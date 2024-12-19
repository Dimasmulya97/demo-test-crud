package com.test.crud.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidatorException extends Exception {
    private String message;
    private String value;

    public ValidatorException(String message) {
        this.message = message;
    }
}
