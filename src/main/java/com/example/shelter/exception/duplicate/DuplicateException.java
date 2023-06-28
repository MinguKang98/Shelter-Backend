package com.example.shelter.exception.duplicate;

import com.example.shelter.exception.BaseException;

import java.util.Map;

public class DuplicateException extends BaseException {

    public DuplicateException(String code, String message) {
        super(code, message);
    }

    public DuplicateException(String code, String message, Map<String, String> errors) {
        super(code, message, errors);
    }

}
