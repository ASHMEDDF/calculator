package com.raven.calculator.config.exception;

import com.raven.calculator.dto.OperationType;

public class OperationNotSupportedException extends RuntimeException {

    public OperationNotSupportedException(OperationType type) {
        super("Operation not supported: " + type);
    }
}
