package com.powder.server.logic;
import org.json.JSONObject;

import java.io.*;
import java.util.Scanner;

public class ResourceManager {
    private String name;
    private String pathToDir;


    public ResourceManager(String pathToDir, String name){
        this.name = name;
        this.pathToDir = ClassLoader.getSystemResource("").getPath() + pathToDir;
    }

    public String readFromResource() throws IOException {
        File file = new File(pathToDir + name + ".json");
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter("\\Z");
        return scanner.next();
    }

    public void saveJSONToResource(JSONObject json) throws IOException {
        File file = new File(pathToDir);
        if(!file.exists()){
            file.mkdirs();
        }

        Writer writer = null;
        try{
            writer = new FileWriter(pathToDir + name + ".json");
            writer.write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(writer != null){
                writer.close();
            }
        }
    }

    public void removeFile(){
        File file = new File(pathToDir + name + ".json");
        if(file.exists()){
            file.delete();
        }
    }
}
