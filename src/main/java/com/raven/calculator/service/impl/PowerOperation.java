package com.raven.calculator.service.impl;

import com.raven.calculator.entity.OperationTypeEnum;
import com.raven.calculator.service.OperationStrategy;

import java.math.BigDecimal;

public class PowerOperation implements OperationStrategy {

    @Override
    public OperationTypeEnum getType() {
        return OperationTypeEnum.POWER;
    }

    @Override
    public BigDecimal apply(BigDecimal a, BigDecimal b) {
        return BigDecimal.valueOf(Math.pow(a.doubleValue(), b.doubleValue()));
    }
}
