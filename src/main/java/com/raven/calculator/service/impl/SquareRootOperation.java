package com.raven.calculator.service.impl;

import com.raven.calculator.dto.OperationType;
import com.raven.calculator.service.OperationStrategy;

import java.math.BigDecimal;

public class SquareRootOperation implements OperationStrategy {
    @Override
    public OperationType getType() {
        return OperationType.SQUARE_ROOT;
    }

    @Override
    public BigDecimal apply(BigDecimal a, BigDecimal b) {
        if (a.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Root of negative number");

        return BigDecimal.valueOf(Math.sqrt(a.doubleValue()));
    }
}
