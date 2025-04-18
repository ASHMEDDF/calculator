package com.raven.calculator.controller;

import com.raven.calculator.dto.CalculateRequest;
import com.raven.calculator.dto.response.OperationResponse;
import com.raven.calculator.entity.OperationEntity;
import com.raven.calculator.service.CalculateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Calculator", description = "Endpoints para realizar operaciones aritméticas")
@RestController
@RequestMapping("/api")
public class CalculateController {

    private final CalculateService calcService;

    public CalculateController(CalculateService calcService) {
        this.calcService = calcService;
    }

    @PostMapping("/calculate")
    @Operation(
            summary = "Realizar cálculo",
            description = "Ejecuta una operación aritmética (suma, resta, ...) ",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Parámetros de la operación",
                    required    = true,
                    content     = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = CalculateRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cálculo OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = OperationResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
                    @ApiResponse(responseCode = "401", description = "Sin autorización")
            }
    )
    public ResponseEntity<OperationResponse> calculate(
            @Valid @jakarta.validation.constraints.NotNull CalculateRequest req) {

        OperationEntity op = calcService.calculate(req);
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