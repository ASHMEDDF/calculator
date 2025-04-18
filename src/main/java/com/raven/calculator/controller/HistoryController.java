package com.raven.calculator.controller;

import com.raven.calculator.dto.response.OperationResponse;
import com.raven.calculator.entity.OperationTypeEnum;
import com.raven.calculator.security.JwtUtil;
import com.raven.calculator.service.HistoryService;
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
    private final JwtUtil jwtUtil;

    public HistoryController(HistoryService historyService, JwtUtil jwtUtil) {
        this.historyService = historyService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public Page<OperationResponse> getHistory(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(name = "operationType", required = false)
            OperationTypeEnum operationType,

            @RequestParam(name = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Instant startDate,

            @RequestParam(name = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Instant endDate,

            Pageable pageable
    ) {
        String token = authHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        Long userId = historyService.findUserIdByUsername(username);

        return historyService.findHistory(userId, operationType, startDate, endDate, pageable)
                .map(OperationResponse::fromEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        historyService.deleteOperation(id);
        return ResponseEntity.noContent().build();
    }
}