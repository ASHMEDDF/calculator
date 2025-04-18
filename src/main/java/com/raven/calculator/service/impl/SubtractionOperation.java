package com.raven.calculator.service.impl;

import com.raven.calculator.entity.OperationTypeEnum;
import com.raven.calculator.service.OperationStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SubtractionOperation implements OperationStrategy {

    public OperationTypeEnum getType() {
        return OperationTypeEnum.SUBTRACTION;
    }

    public BigDecimal apply(BigDecimal a, BigDecimal b) {
        return a.subtract(b);
    }
}
