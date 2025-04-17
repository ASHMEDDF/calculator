package com.raven.calculator.repository;

import com.raven.calculator.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OperationRepository extends JpaRepository<Operation, UUID> {
}
