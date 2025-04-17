package com.raven.calculator.service.impl;

import com.raven.calculator.dto.OperationType;
import com.raven.calculator.service.OperationStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SubtractionOperation implements OperationStrategy {

    public OperationType getType() {
        return OperationType.SUBTRACTION;
    }

    public BigDecimal apply(BigDecimal a, BigDecimal b) {
        return a.subtract(b);
    }
}
