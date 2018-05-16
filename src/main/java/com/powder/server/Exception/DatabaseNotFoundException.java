package com.powder.server.Exception;

public class DatabaseNotFoundException extends FakeSQLException{

    private String databaseName;

    public DatabaseNotFoundException(String databaseName){
        this.databaseName = databaseName;
    }


    @Override
    public String getReason() {
        return "Database " + databaseName + " not found";
    }
}
