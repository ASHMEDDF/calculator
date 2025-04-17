package com.raven.calculator.entity;

import com.raven.calculator.dto.OperationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "operations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Operation {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationType operationType;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal operandA;

    @Column(precision = 19, scale = 8)
    private BigDecimal operandB;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal result;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false)
    private Long userId;
}