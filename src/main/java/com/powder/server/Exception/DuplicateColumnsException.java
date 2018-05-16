package com.powder.server.Exception;

public class DuplicateColumnsException extends FakeSQLException{

    private String columnName;

    @Override
    public String getReason() {
        return "Table consists of columns which have the same names. It is not allowed.";
    }
}
