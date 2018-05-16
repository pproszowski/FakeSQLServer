package com.powder.server.Exception;

public class CurrentDatabaseNotSetException extends FakeSQLException{

    @Override
    public String getReason() {
        return "No current database set";
    }
}
