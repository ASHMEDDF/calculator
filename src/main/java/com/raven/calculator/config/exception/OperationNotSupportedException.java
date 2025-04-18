package com.raven.calculator.config.exception;

import com.raven.calculator.entity.OperationTypeEnum;

public class OperationNotSupportedException extends RuntimeException {

    public OperationNotSupportedException(OperationTypeEnum type) {
        super("Operation not supported: " + type);
    }
}
