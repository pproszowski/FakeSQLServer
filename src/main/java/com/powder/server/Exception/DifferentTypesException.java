package com.powder.server.Exception;

public class DifferentTypesException extends Throwable {
    @Override
    public String getMessage() {
        return "Different types Exception";
    }
}
