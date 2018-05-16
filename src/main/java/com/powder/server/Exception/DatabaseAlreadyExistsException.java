package com.powder.server.Exception;

public class DatabaseAlreadyExistsException extends FakeSQLException{

    private String databaseName;
    public DatabaseAlreadyExistsException(String databaseName){
        this.databaseName = databaseName;
    }

    @Override
    public String getReason() {
        return "Database '" + databaseName + "' already exists";
    }
}
