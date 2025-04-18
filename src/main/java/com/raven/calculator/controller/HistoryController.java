package com.raven.calculator.controller;

import com.raven.calculator.dto.response.OperationResponse;
import com.raven.calculator.entity.OperationTypeEnum;
import com.raven.calculator.security.JwtUtil;
import com.raven.calculator.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryService historyService;
    private final JwtUtil        jwtUtil;

    public HistoryController(HistoryService historyService,
                             JwtUtil jwtUtil) {
        this.historyService  = historyService;
        this.jwtUtil         = jwtUtil;
    }

    @GetMapping
    @Operation(
            summary = "Listar historial de operaciones",
            description = "Devuelve una página de operaciones del usuario actual, permite filtrar por tipo y/o rango de fechas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado paginado de operaciones"),
                    @ApiResponse(responseCode = "401", description = "Token inválido o ausente")
            }
    )
    public Page<OperationResponse> getHistory(
            @RequestHeader("Authorization")
            @Parameter(description = "Bearer JWT", in = ParameterIn.HEADER) String authHeader,

            @RequestParam(name = "operationType", required = false)
            @Parameter(description = "Tipo de operación a filtrar", example = "ADDITION") OperationTypeEnum operationType,

            @RequestParam(name = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Parameter(description = "Fecha inicial ISO-8601", example = "2025-04-01T00:00:00Z") Instant startDate,

            @RequestParam(name = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Parameter(description = "Fecha final ISO-8601", example = "2025-04-30T23:59:59Z") Instant endDate,

            @Parameter(hidden = true) Pageable pageable
    ) {
        String token = authHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        Long userId = historyService.findUserIdByUsername(username);

        return historyService.findHistory(userId, operationType, startDate, endDate, pageable)
                .map(OperationResponse::fromEntity);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar operación del historial",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Operación eliminada"),
                    @ApiResponse(responseCode = "404", description = "No se encontró la operación"),
                    @ApiResponse(responseCode = "401", description = "Token inválido o ausente")
            }
    )
    public ResponseEntity<Void> deleteHistory(
            @RequestHeader("Authorization")
            @Parameter(description = "Bearer JWT", in = ParameterIn.HEADER) String authHeader,

            @PathVariable
            @Parameter(description = "ID de la operación a eliminar", example = "42") Long id
    ) {
        historyService.deleteOperation(id);
        return ResponseEntity.noContent().build();
    }
}