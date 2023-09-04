package com.example.shelter.exception.notfound;

import com.example.shelter.exception.BaseException;

import java.util.Map;

public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String code, String message) {
        super(code, message);
    }

    public ResourceNotFoundException(String code, String message, Map<String, String> errors) {
        super(code, message, errors);
    }

}
