package com.raven.calculator.service.impl;

import com.raven.calculator.dto.OperationType;
import com.raven.calculator.service.OperationStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AdditionOperation implements OperationStrategy {

    @Override
    public OperationType getType() {
        return OperationType.ADDITION;
    }

    @Override
    public BigDecimal apply(BigDecimal a, BigDecimal b) {
        return a.add(b);
    }
}
