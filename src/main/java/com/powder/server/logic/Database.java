package com.powder.server.logic;
import com.powder.server.Exception.TableAlreadyExistsException;
import com.powder.server.Exception.TableNotFoundException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<Table> tables;
    private String name;

    public Database(String _name){
        name = _name;
        tables = new ArrayList<>();
    }

    public Database(JSONObject database) throws JSONException, IOException {
        name = database.getString("Name");
        tables = new ArrayList<>();
        JSONArray jsonTables = database.getJSONArray("Tables");
        List<String> tableNames = new ArrayList<>();
        for(int i = 0; i < jsonTables.length(); i++){
            tableNames.add(jsonTables.getString(i));
        }
        for(String tableName : tableNames){
            ResourceManager resourceManager = new ResourceManager("res/Databases/Tables/" + getName() + "_", tableName);
            JSONObject jsonTable = new JSONObject(resourceManager.readFromResource());
            Table table = new Table(jsonTable);
            tables.add(table);
        }
    }

    public void addTable(Table _table) throws TableAlreadyExistsException {
        for(Table table : tables){
            if(table.getName().equals(_table.getName())){
                throw new TableAlreadyExistsException(table.getName() ,this.getName());
            }
        }
        tables.add(_table);
    }

    public void removeTable(String tableName) throws TableNotFoundException, JSONException, IOException {
        Table tableToDelete = null;
        for(Table table : tables){
            if(table.getName().equals(tableName)){
                tableToDelete = table;
            }
        }

        if(tableToDelete != null){
            tables.remove(tableToDelete);
            tableToDelete.deleteFile();
        }else{
            throw new TableNotFoundException(this.getName(), tableName);
        }
    }

    public Table getTable(String tableName) throws TableNotFoundException {
        for(Table table : tables){
            if(table.getName().equalsIgnoreCase(tableName)){
                return table;
            }
        }

        throw new TableNotFoundException(this.getName(), tableName);
    }

    public int howManyTables(){
        return tables.size();
    }

    public String getName() {
        return name;
    }

    public void saveToFile() throws JSONException, IOException {
        ResourceManager resourceManager = new ResourceManager("res/Databases/", name);
        resourceManager.saveJSONToResource(toJson());


    }

    public void deleteFile(){
        ResourceManager resourceManager = new ResourceManager("res/Databases/", name);
        resourceManager.removeFile();
    }

    public void deleteTables() {
        for(Table table : tables){
            ResourceManager resourceManager = new ResourceManager("res/Databases/Tables/", table.getName());
            resourceManager.removeFile();
        }
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonDatabase = new JSONObject();
        jsonDatabase.put("Name", name);
        JSONArray jsonTableNames = new JSONArray();
        for(Table table : tables){
            jsonTableNames.put(table.getName());
        }
        jsonDatabase.put("Tables", jsonTableNames);
        return jsonDatabase;
    }
}
