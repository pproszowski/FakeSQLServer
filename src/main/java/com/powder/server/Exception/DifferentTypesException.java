package com.powder.server.Exception;

public class DifferentTypesException extends FakeSQLException{

    private String first;
    private String second;
    public DifferentTypesException(String first, String second){
        this.first = first;
        this.second = second;
    }

    @Override
    public String getReason() {
        return "Different types: '" + first + "' and '" + second + "'";
    }
}
