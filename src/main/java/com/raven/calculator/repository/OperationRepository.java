package com.raven.calculator.repository;

import com.raven.calculator.entity.OperationEntity;
import com.raven.calculator.entity.OperationTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface OperationRepository extends JpaRepository<OperationEntity, Long> {

    Page<OperationEntity> findByUserId(Long userId, Pageable pageable);

    @Query("""
       SELECT o
         FROM Operation o
        WHERE o.userId = :userId
          AND ( :operationType IS NULL OR o.operationType = :operationType )
          AND ( :startDate     IS NULL OR o.timestamp      >= :startDate     )
          AND ( :endDate       IS NULL OR o.timestamp      <= :endDate       )
    """)
    Page<OperationEntity> findHistoryFiltered(
            @Param("userId")         Long                userId,
            @Param("operationType")  OperationTypeEnum   operationType,
            @Param("startDate")      Instant             startDate,
            @Param("endDate")        Instant             endDate,
            Pageable            pageable
    );
}
