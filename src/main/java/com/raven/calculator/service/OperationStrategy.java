package com.raven.calculator.service;

import com.raven.calculator.entity.OperationTypeEnum;

import java.math.BigDecimal;

public interface OperationStrategy {

    OperationTypeEnum getType();
    BigDecimal apply(BigDecimal a, BigDecimal b);
}