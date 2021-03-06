package com.powder.server.Exception;

public class TableAlreadyExistsException extends FakeSQLException{

    private String tableName;
    private String databaseName;
    public TableAlreadyExistsException(String _tableName, String _databaseName){
       tableName = _tableName;
       databaseName = _databaseName;
    }

    @Override
    public String getReason() {
        return "'" + tableName + "' already exists in '" + databaseName + "'.";
    }
}
