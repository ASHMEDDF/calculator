package com.raven.calculator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity(name = "Operation")
@Table(name = "operations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OperationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationTypeEnum operationType;

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