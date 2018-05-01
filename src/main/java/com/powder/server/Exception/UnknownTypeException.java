package com.powder.server.Exception;

public class UnknownTypeException extends Throwable {
    @Override
    public String getMessage() {
        return "Error: unknown type of variable";
    }
}
