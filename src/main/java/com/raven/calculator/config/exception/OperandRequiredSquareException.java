package com.raven.calculator.config.exception;

import com.raven.calculator.entity.OperationTypeEnum;

public class OperandRequiredSquareException extends RuntimeException {

    public OperandRequiredSquareException(OperationTypeEnum type) {
        super("operandB is required for " + type);
    }
}
