package com.raven.calculator.entity;

import com.raven.calculator.dto.OperationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
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