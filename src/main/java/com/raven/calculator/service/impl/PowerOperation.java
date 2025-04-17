package com.raven.calculator.service.impl;

import com.raven.calculator.dto.OperationType;
import com.raven.calculator.service.OperationStrategy;

import java.math.BigDecimal;

public class PowerOperation implements OperationStrategy {

    @Override
    public OperationType getType() {
        return OperationType.POWER;
    }

    @Override
    public BigDecimal apply(BigDecimal a, BigDecimal b) {
        return BigDecimal.valueOf(Math.pow(a.doubleValue(), b.doubleValue()));
    }
}
