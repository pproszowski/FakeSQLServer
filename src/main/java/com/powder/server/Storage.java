package com.powder.server;
import com.powder.server.Exception.CurrentDatabaseNotSetException;
import com.powder.server.Exception.DatabaseAlreadyExistsException;
import com.powder.server.Exception.DatabaseNotFoundException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Storage {
    private String name;
    private List<Database> databases;
    private Database currentDatabase;

    private Storage(String _name) throws IOException, JSONException {
        name = _name;
        databases = new ArrayList<>();
    }

    private Storage(JSONObject jsonStorage) throws JSONException, IOException {
        databases = new ArrayList<>();
        System.out.println(jsonStorage.toString());
        name = jsonStorage.getString("Name");
        JSONArray jsonDatabaseNames = jsonStorage.getJSONArray("DatabaseNames");
        List<String> databaseNames = new ArrayList<>();
        for(int i = 0; i < jsonDatabaseNames.length(); i++){
            databaseNames.add(jsonDatabaseNames.getString(i));
        }

        for(String databaseName : databaseNames){
            ResourceManager resourceManager = new ResourceManager("res/Databases/",databaseName);
            JSONObject jsonDatabase = new JSONObject(resourceManager.readFromResource());
            Database database = new Database(jsonDatabase);
            databases.add(database);
        }
        String currentDatabaseName = jsonStorage.getString("CurrentDatabase");
        if(!currentDatabaseName.equals("null")){
            for(Database database : databases){
                if(database.getName().equalsIgnoreCase(currentDatabaseName)){
                    currentDatabase = database;
                }
            }
        }
    }

    public static Storage getInstance() {
        Storage storage = null;
        File file = new File(ClassLoader.getSystemResource("").getPath() + "res" + "/storage.json");
        try {
            if(file.exists()){
                    Scanner scanner = new Scanner(file);
                    scanner.useDelimiter("\\Z");
                    JSONObject jsonStorage= new JSONObject(scanner.next());
                    storage = new Storage(jsonStorage);
            }else{
                    storage = new Storage("storage");
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return storage;
    }

    public void addDatabase(Database _database) throws DatabaseAlreadyExistsException, JSONException, IOException {
        for(Database database : databases){
            if(database.getName().equals(_database.getName())){
                throw new DatabaseAlreadyExistsException();
            }
        }

        if(databases.isEmpty()){
            currentDatabase = _database;
        }
        databases.add(_database);
    }

    public void deleteDatabase(String whichDatabase) throws DatabaseNotFoundException, JSONException, IOException {
        Database toDelete = null;
        for(Database database : databases){
            if(database.getName().equals(whichDatabase)){
                toDelete = database;
            }
        }
        if(toDelete != null){
            databases.remove(toDelete);
            toDelete.deleteTables();
            toDelete.deleteFile();
            if(databases.isEmpty()){
                currentDatabase = null;
            }
        }else{
            throw new DatabaseNotFoundException();
        }
    }

    public void deleteAllDatabases() throws IOException, JSONException, DatabaseNotFoundException {
        for(Database database : databases){
            deleteDatabase(database.getName());
        }
    }

    public Response executeQuery(Query query) throws JSONException, IOException {
        return query.execute(this);
    }

    public int howManyDatabases(){
        return databases.size();
    }

    public void setCurrentDatabase(String databaseName) throws DatabaseNotFoundException, IOException, JSONException {
        for(Database database : databases){
            if(database.getName().equals(databaseName)){
                currentDatabase = database;
                return;
            }
        }
        throw new DatabaseNotFoundException();
    }

    public Database getCurrentDatabase() throws CurrentDatabaseNotSetException {
        if(currentDatabase != null){
            return currentDatabase;
        }else{
            throw new CurrentDatabaseNotSetException();
        }
    }

    public void saveToFile() throws JSONException, IOException {

        ResourceManager resourceManager = new ResourceManager("res/", name);
        resourceManager.saveJSONToResource(this.toJSON());
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonStorage = new JSONObject();
        JSONArray jsonDatabases = new JSONArray();
        for(Database database : databases){
            jsonDatabases.put(database.getName());
        }
        jsonStorage.put("Name", name);
        jsonStorage.put( "CurrentDatabase", currentDatabase != null ? currentDatabase.getName() : "null");
        jsonStorage.put("DatabaseNames", jsonDatabases);
        return jsonStorage;
    }
}
