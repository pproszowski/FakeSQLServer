package com.powder.server.Exception;

public class UnknownTypeException extends FakeSQLException{

    private String typeName;

    public UnknownTypeException(String typeName){
        this.typeName = typeName;
    }

    @Override
    public String getReason() {
        return "Cannot recognize type " + "'" + typeName + "'.";
    }
}
