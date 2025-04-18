package com.raven.calculator.repository;

import com.raven.calculator.entity.Operation;
import com.raven.calculator.entity.OperationTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface OperationRepository extends JpaRepository<Operation, Long> {

    Page<Operation> findByUserId(Long userId, Pageable pageable);

    @Query("""
      select o
        from Operation o
       where o.userId = :userId
         and (:type  is null or o.operationType = :type)
         and (:from  is null or o.timestamp >= :from)
         and (:to    is null or o.timestamp <= :to)
    """)
    Page<Operation> findHistoryFiltered(
            @Param("userId") Long userId,
            @Param("type") OperationTypeEnum type,
            @Param("from") Instant from,
            @Param("to")     Instant to,
            Pageable        pageable
    );
}
