package com.powder.server.Exception;

public abstract class FakeSQLException extends Exception{
    public abstract String getReason();
}
