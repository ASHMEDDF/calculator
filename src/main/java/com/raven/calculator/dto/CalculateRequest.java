package com.raven.calculator.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class CalculateRequest {

    @NotNull
    private OperationType operation;

    @NotNull
    @DecimalMin(value = "-1000000", inclusive = true,
            message = "operandA must be ≥ -1,000,000")
    @DecimalMax(value = "1000000", inclusive = true,
            message = "operandA must be ≤ 1,000,000")
    private BigDecimal operandA;

    @DecimalMin(value = "-1000000", inclusive = true,
            message = "operandB must be ≥ -1,000,000")
    @DecimalMax(value = "1000000", inclusive = true,
            message = "operandB must be ≤ 1,000,000")
    private BigDecimal operandB;

}