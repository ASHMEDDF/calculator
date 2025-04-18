package com.raven.calculator.service;

import com.raven.calculator.config.exception.DivisionByZeroException;
import com.raven.calculator.config.exception.OperandRequiredSquareException;
import com.raven.calculator.config.exception.OperationNotSupportedException;
import com.raven.calculator.config.exception.RootOfNegativeException;
import com.raven.calculator.dto.CalculateRequest;
import com.raven.calculator.entity.OperationEntity;
import com.raven.calculator.entity.OperationTypeEnum;
import com.raven.calculator.entity.User;
import com.raven.calculator.repository.OperationRepository;
import com.raven.calculator.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class CalculateServiceTests {

    @Mock
    OperationRepository opRepo;
    @Mock
    UserRepository userRepo;
    @Mock
    OperationStrategy additionStrat;
    @Mock
    OperationStrategy subtractionStrat;
    @Mock
    OperationStrategy multiplicationStrat;
    @Mock
    OperationStrategy divisionStrat;
    @Mock
    OperationStrategy squareRootStrat;
    @Mock
    OperationStrategy powerStrat;
    
    private List<OperationStrategy> strategies;
    private CalculateService calculateService;

    @BeforeEach
    void setup() {
        // Setup strategy types
        lenient().when(additionStrat.getType()).thenReturn(OperationTypeEnum.ADDITION);
        lenient().when(subtractionStrat.getType()).thenReturn(OperationTypeEnum.SUBTRACTION);
        lenient().when(multiplicationStrat.getType()).thenReturn(OperationTypeEnum.MULTIPLICATION);
        lenient().when(divisionStrat.getType()).thenReturn(OperationTypeEnum.DIVISION);
        lenient().when(squareRootStrat.getType()).thenReturn(OperationTypeEnum.SQUARE_ROOT);
        lenient().when(powerStrat.getType()).thenReturn(OperationTypeEnum.POWER);

        // Create list of strategies
        strategies = Arrays.asList(
            additionStrat,
            subtractionStrat,
            multiplicationStrat,
            divisionStrat,
            squareRootStrat,
            powerStrat
        );

        // Create service instance with strategies
        calculateService = new CalculateService(strategies, opRepo, userRepo);

        // Setup security context
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("testuser")
                .password("password")
                .authorities("ROLE_USER")
                .build();
        
        Authentication authentication = mock(Authentication.class);
        lenient().when(authentication.getPrincipal()).thenReturn(userDetails);
        
        SecurityContext securityContext = mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Setup user repository
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        lenient().when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));
    }

    private CalculateRequest createRequest(OperationTypeEnum operation, BigDecimal a, BigDecimal b) {
        CalculateRequest req = new CalculateRequest();
        req.setOperation(operation);
        req.setOperandA(a);
        req.setOperandB(b);
        return req;
    }

    private OperationEntity createOperation(OperationTypeEnum type, BigDecimal a, BigDecimal b, BigDecimal result) {
        OperationEntity op = new OperationEntity();
        op.setId(UUID.randomUUID());
        op.setOperationType(type);
        op.setOperandA(a);
        op.setOperandB(b);
        op.setResult(result);
        return op;
    }

    @Test
    void addition_happyPath() {
        // Given
        when(additionStrat.apply(any(), any())).thenReturn(new BigDecimal("8.0"));
        
        CalculateRequest req = createRequest(OperationTypeEnum.ADDITION,
            new BigDecimal("5"), new BigDecimal("3"));
        OperationEntity expectedOp = createOperation(OperationTypeEnum.ADDITION,
            new BigDecimal("5"), new BigDecimal("3"), new BigDecimal("8.0"));
        when(opRepo.save(any())).thenReturn(expectedOp);

        // When
        OperationEntity result = calculateService.calculate(req);

        // Then
        assertThat(result.getResult()).isEqualByComparingTo("8.0");
        verify(opRepo).save(any());
    }

    @Test
    void subtraction_happyPath() {
        // Given
        when(subtractionStrat.apply(any(), any())).thenReturn(new BigDecimal("2.0"));
        
        CalculateRequest req = createRequest(OperationTypeEnum.SUBTRACTION,
            new BigDecimal("5"), new BigDecimal("3"));
        OperationEntity expectedOp = createOperation(OperationTypeEnum.SUBTRACTION,
            new BigDecimal("5"), new BigDecimal("3"), new BigDecimal("2.0"));
        when(opRepo.save(any())).thenReturn(expectedOp);

        // When
        OperationEntity result = calculateService.calculate(req);

        // Then
        assertThat(result.getResult()).isEqualByComparingTo("2.0");
        verify(opRepo).save(any());
    }

    @Test
    void multiplication_happyPath() {
        // Given
        when(multiplicationStrat.apply(any(), any())).thenReturn(new BigDecimal("15.0"));
        
        CalculateRequest req = createRequest(OperationTypeEnum.MULTIPLICATION,
            new BigDecimal("5"), new BigDecimal("3"));
        OperationEntity expectedOp = createOperation(OperationTypeEnum.MULTIPLICATION,
            new BigDecimal("5"), new BigDecimal("3"), new BigDecimal("15.0"));
        when(opRepo.save(any())).thenReturn(expectedOp);

        // When
        OperationEntity result = calculateService.calculate(req);

        // Then
        assertThat(result.getResult()).isEqualByComparingTo("15.0");
        verify(opRepo).save(any());
    }

    @Test
    void division_happyPath() {
        // Given
        when(divisionStrat.apply(any(), any())).thenReturn(new BigDecimal("2.0"));
        
        CalculateRequest req = createRequest(OperationTypeEnum.DIVISION,
            new BigDecimal("6"), new BigDecimal("3"));
        OperationEntity expectedOp = createOperation(OperationTypeEnum.DIVISION,
            new BigDecimal("6"), new BigDecimal("3"), new BigDecimal("2.0"));
        when(opRepo.save(any())).thenReturn(expectedOp);

        // When
        OperationEntity result = calculateService.calculate(req);

        // Then
        assertThat(result.getResult()).isEqualByComparingTo("2.0");
        verify(opRepo).save(any());
    }

    @Test
    void squareRoot_happyPath() {
        // Given
        when(squareRootStrat.apply(any(), any())).thenReturn(new BigDecimal("3.0"));
        
        CalculateRequest req = createRequest(OperationTypeEnum.SQUARE_ROOT,
            new BigDecimal("9"), null);
        OperationEntity expectedOp = createOperation(OperationTypeEnum.SQUARE_ROOT,
            new BigDecimal("9"), null, new BigDecimal("3.0"));
        when(opRepo.save(any())).thenReturn(expectedOp);

        // When
        OperationEntity result = calculateService.calculate(req);

        // Then
        assertThat(result.getResult()).isEqualByComparingTo("3.0");
        verify(opRepo).save(any());
    }

    @Test
    void power_happyPath() {
        // Given
        when(powerStrat.apply(any(), any())).thenReturn(new BigDecimal("8.0"));
        
        CalculateRequest req = createRequest(OperationTypeEnum.POWER,
            new BigDecimal("2"), new BigDecimal("3"));
        OperationEntity expectedOp = createOperation(OperationTypeEnum.POWER,
            new BigDecimal("2"), new BigDecimal("3"), new BigDecimal("8.0"));
        when(opRepo.save(any())).thenReturn(expectedOp);

        // When
        OperationEntity result = calculateService.calculate(req);

        // Then
        assertThat(result.getResult()).isEqualByComparingTo("8.0");
        verify(opRepo).save(any());
    }

    @Test
    void divisionByZero_throws() {
        // Given
        CalculateRequest req = createRequest(OperationTypeEnum.DIVISION,
            BigDecimal.TEN, BigDecimal.ZERO);

        // Then
        assertThrows(DivisionByZeroException.class, () -> calculateService.calculate(req));
        verify(opRepo, never()).save(any());
    }

    @Test
    void squareRootOfNegative_throws() {
        // Given
        CalculateRequest req = createRequest(OperationTypeEnum.SQUARE_ROOT,
            new BigDecimal("-9"), null);

        // Then
        assertThrows(RootOfNegativeException.class, () -> calculateService.calculate(req));
        verify(opRepo, never()).save(any());
    }

    @Test
    void operandAOutOfRange_throws() {
        // Given
        CalculateRequest req = createRequest(OperationTypeEnum.ADDITION,
            new BigDecimal("-1000001"), BigDecimal.ONE);

        // Then
        assertThrows(IllegalArgumentException.class, () -> calculateService.calculate(req));
        verify(opRepo, never()).save(any());
    }

    @Test
    void operandBOutOfRange_throws() {
        // Given
        CalculateRequest req = createRequest(OperationTypeEnum.ADDITION,
            BigDecimal.ONE, new BigDecimal("1000001"));

        // Then
        assertThrows(IllegalArgumentException.class, () -> calculateService.calculate(req));
        verify(opRepo, never()).save(any());
    }

    @Test
    void operationNotSupported_throws() {
        // Given
        CalculateRequest req = createRequest(null, BigDecimal.ONE, BigDecimal.ONE);

        // Then
        assertThrows(OperationNotSupportedException.class, () -> calculateService.calculate(req));
        verify(opRepo, never()).save(any());
    }

    @Test
    void squareRootRequiresOperandB_throws() {
        // Given
        CalculateRequest req = createRequest(OperationTypeEnum.SQUARE_ROOT,
            BigDecimal.ONE, BigDecimal.ONE);

        // Then
        assertThrows(OperandRequiredSquareException.class, () -> calculateService.calculate(req));
        verify(opRepo, never()).save(any());
    }
}

