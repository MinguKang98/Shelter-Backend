package com.example.shelter.exception.notfound;

public class ResourceNotFoundException extends RuntimeException{

    private String errorCode;
    private String message;

    public ResourceNotFoundException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
