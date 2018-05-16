package com.powder.server.Exception;
public class BadQueryTypeException extends FakeSQLException{

    @Override
    public String getReason() {
        return "Bad Query Type";
    }
}
