package com.raven.calculator.service;

import com.raven.calculator.dto.OperationType;

import java.math.BigDecimal;

public interface OperationStrategy {

    OperationType getType();
    BigDecimal apply(BigDecimal a, BigDecimal b);
}