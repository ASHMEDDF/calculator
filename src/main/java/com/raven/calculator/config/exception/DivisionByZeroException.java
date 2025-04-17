package com.raven.calculator.config.exception;

public class DivisionByZeroException extends RuntimeException {

    public DivisionByZeroException() {
        super("Division by zero is not allowed");
    }
}
