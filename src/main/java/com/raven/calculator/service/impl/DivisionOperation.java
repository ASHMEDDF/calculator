package com.raven.calculator.service.impl;

import com.raven.calculator.entity.OperationTypeEnum;
import com.raven.calculator.service.OperationStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Component
public class DivisionOperation implements OperationStrategy {

    private static final MathContext MC = new MathContext(16, RoundingMode.HALF_UP);

    @Override
    public OperationTypeEnum getType() {
        return OperationTypeEnum.DIVISION;
    }

    @Override
    public BigDecimal apply(BigDecimal a, BigDecimal b) {
        if (b.compareTo(BigDecimal.ZERO) == 0)
            throw new IllegalArgumentException("Division by zero");

        return a.divide(b, MC);
    }
}
