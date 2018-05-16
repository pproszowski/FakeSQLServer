package com.powder.server.Exception;

public class ColumnNotFoundException extends FakeSQLException{

    private String columnName;
    private String tableName;

    public ColumnNotFoundException(String columnName, String tableName){
        this.columnName = columnName;
        this.tableName = tableName;
    }

    @Override
    public String getReason() {
        return "Column '" + columnName + "' not found in '" + tableName + "'";
    }
}
