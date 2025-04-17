package com.raven.calculator.config.exception;

public class OperandOutOfRangeException extends RuntimeException {

    public OperandOutOfRangeException(String operand, String message) {
        super(operand + " " + message);
    }
}