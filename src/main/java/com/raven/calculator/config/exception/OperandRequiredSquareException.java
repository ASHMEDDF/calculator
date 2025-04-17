package com.raven.calculator.config.exception;

import com.raven.calculator.dto.OperationType;

public class OperandRequiredSquareException extends RuntimeException {

    public OperandRequiredSquareException(OperationType type) {
        super("operandB is required for " + type);
    }
}
