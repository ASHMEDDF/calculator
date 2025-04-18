package com.raven.calculator.service;

import com.raven.calculator.dto.response.OperationResponse;
import com.raven.calculator.entity.OperationEntity;
import com.raven.calculator.entity.OperationTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface HistoryService {


    /**
     * Devuelve la página de operaciones del usuario (filtrada opcionalmente por tipo y fechas).
     *
     * @param username      el nombre de usuario (extraído del JWT)
     * @param operationType opcional, filtrar por tipo de operación
     * @param startDate     opcional, operaciones >= esta fecha
     * @param endDate       opcional, operaciones <= esta fecha
     * @param pageable      paginación y ordenación
     */
    Page<OperationResponse> getHistory(
            String username,
            OperationTypeEnum operationType,
            Instant startDate,
            Instant endDate,
            Pageable pageable
    );

    Page<OperationEntity> findHistory(Long userId,
                                      OperationTypeEnum type,
                                      Instant from,
                                      Instant to,
                                      Pageable pageable);

    Long findUserIdByUsername(String username);

    void deleteOperation(Long id);
}