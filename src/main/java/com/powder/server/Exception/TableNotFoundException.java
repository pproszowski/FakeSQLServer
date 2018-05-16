package com.powder.server.Exception;

public class TableNotFoundException extends FakeSQLException{
    private String databaseName;
    private String tableName;
    public TableNotFoundException(String _databaseName, String _tableName){
        databaseName = _databaseName;
        tableName = _tableName;
    }
    @Override
    public String getReason() {
        return "Error: unable to find table '" + tableName + "' in database '" + databaseName + "'.";
    }
}
