package com.raven.calculator.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    private ResponseEntity<Map<String,Object>> buildResponse(HttpStatus status, String message) {
        Map<String,Object> body = Map.of(
                "timestamp", Instant.now(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(DivisionByZeroException.class)
    public ResponseEntity<Map<String,Object>> handleDivZero(DivisionByZeroException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(OperandOutOfRangeException.class)
    public ResponseEntity<Map<String,Object>> handleRange(OperandOutOfRangeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(OperandRequiredSquareException.class)
    public ResponseEntity<Map<String,Object>> operanReqSquare(OperandRequiredSquareException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(OperationNotSupportedException.class)
    public ResponseEntity<Map<String,Object>> handleNotSupported(OperationNotSupportedException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(RootOfNegativeException.class)
    public ResponseEntity<Map<String,Object>> rootNegative(RootOfNegativeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String,Object>> handleOther(RuntimeException ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error");
    }
}
