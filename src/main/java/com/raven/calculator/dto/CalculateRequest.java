package com.raven.calculator.dto;

import com.raven.calculator.entity.OperationTypeEnum;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CalculateRequest {

    @NotNull
    private OperationTypeEnum operation;

    @NotNull
    @DecimalMin(value = "-1000000",
            message = "operandA must be ≥ -1,000,000")
    @DecimalMax(value = "1000000",
            message = "operandA must be ≤ 1,000,000")
    private BigDecimal operandA;

    @DecimalMin(value = "-1000000",
            message = "operandB must be ≥ -1,000,000")
    @DecimalMax(value = "1000000",
            message = "operandB must be ≤ 1,000,000")
    private BigDecimal operandB;

}