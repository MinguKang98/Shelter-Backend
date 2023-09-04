package com.example.shelter.exception.badinput;

import java.util.Map;

public class NumberOfRowsException extends BadInputException {

    public NumberOfRowsException(int numberOfRows) {
        super("301", "행 갯수는 1이상 1000이하입니다.", Map.of("numberOfRows", String.valueOf(numberOfRows)));
    }

}
