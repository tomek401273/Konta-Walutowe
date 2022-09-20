package com.tgrajkowski.exception;

public class DataConsistencyException extends RuntimeException {
    public DataConsistencyException(String message) {
        super(message);
    }
}
