package com.raven.calculator.config.exception;

public class RootOfNegativeException extends RuntimeException {

    public RootOfNegativeException() {
        super("Square root of negative is not allowed");
    }
}
