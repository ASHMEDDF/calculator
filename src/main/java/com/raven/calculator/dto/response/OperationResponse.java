package com.raven.calculator.dto.response;

import com.raven.calculator.entity.Operation;
import com.raven.calculator.entity.OperationTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@AllArgsConstructor
public class OperationResponse {

    private final Long id;
    private final OperationTypeEnum operation;
    private final BigDecimal operandA;
    private final BigDecimal operandB;
    private final BigDecimal result;
    private final Instant timestamp;
    private final Long userId;

    public static OperationResponse fromEntity(Operation o) {
        return new OperationResponse(
                o.getId(),
                o.getOperationType(),
                o.getOperandA(),
                o.getOperandB(),
                o.getResult(),
                o.getTimestamp(),
                o.getUserId()
        );
    }
}