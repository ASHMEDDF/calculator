package com.raven.calculator.dto.response;

import com.raven.calculator.dto.OperationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class OperationResponse {

    private final UUID id;
    private final OperationType operation;
    private final BigDecimal operandA;
    private final BigDecimal operandB;
    private final BigDecimal result;
    private final Instant timestamp;
    private final Long userId;
}