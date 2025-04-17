package com.raven.calculator.controller;

import com.raven.calculator.dto.CalculateRequest;
import com.raven.calculator.dto.response.OperationResponse;
import com.raven.calculator.entity.Operation;
import com.raven.calculator.service.CalculateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class CalculateController {

    private final CalculateService calcService;

    public CalculateController(CalculateService calcService) {
        this.calcService = calcService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<OperationResponse> calculate(
            @Valid @RequestBody CalculateRequest req) {

        Operation op = calcService.calculate(req);

        OperationResponse resp = new OperationResponse(
                op.getId(),
                op.getOperationType(),
                op.getOperandA(),
                op.getOperandB(),
                op.getResult(),
                op.getTimestamp(),
                op.getUserId()
        );

        return ResponseEntity.ok(resp);
    }
}