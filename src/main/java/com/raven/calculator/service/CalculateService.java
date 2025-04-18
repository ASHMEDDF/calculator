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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CalculateService {

    private static final BigDecimal MIN_VALUE = new BigDecimal("-1000000");
    private static final BigDecimal MAX_VALUE = new BigDecimal("1000000");

    private final Map<OperationTypeEnum, OperationStrategy> strategyMap;
    private final OperationRepository opRepo;
    private final UserRepository userRepo;

    public CalculateService(
            List<OperationStrategy> strategies,
            OperationRepository opRepo,
            UserRepository userRepo
    ) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(OperationStrategy::getType, Function.identity()));
        this.opRepo      = opRepo;
        this.userRepo    = userRepo;
    }

    public OperationEntity calculate(CalculateRequest req) {
        BigDecimal a = req.getOperandA();
        BigDecimal b = req.getOperandB();

        if (a.compareTo(MIN_VALUE) < 0 || a.compareTo(MAX_VALUE) > 0) {
            throw new IllegalArgumentException("operandA must be between -1,000,000 and 1,000,000");
        }

        if (req.getOperation() != OperationTypeEnum.SQUARE_ROOT) {
            if (b == null) {
                throw new OperandRequiredSquareException(req.getOperation());
            }
            if (b.compareTo(MIN_VALUE) < 0 || b.compareTo(MAX_VALUE) > 0) {
                throw new IllegalArgumentException("operandB must be between -1,000,000 and 1,000,000");
            }
        } else if (b != null) {
            throw new OperandRequiredSquareException(req.getOperation());
        }

        OperationStrategy strat = strategyMap.get(req.getOperation());
        if (strat == null) {
            throw new OperationNotSupportedException(req.getOperation());
        }

        if (req.getOperation() == OperationTypeEnum.DIVISION
                && req.getOperandB().compareTo(BigDecimal.ZERO) == 0) {
            throw new DivisionByZeroException();
        }

        if (req.getOperation() == OperationTypeEnum.SQUARE_ROOT
                && req.getOperandA().compareTo(BigDecimal.ZERO) < 0) {
            throw new RootOfNegativeException();
        }

        BigDecimal res = strat.apply(req.getOperandA(), req.getOperandB());

        OperationEntity op = new OperationEntity();
        op.setOperationType(req.getOperation());
        op.setOperandA(a);
        op.setOperandB(b);
        op.setResult(res);
        op.setTimestamp(Instant.now());

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = (principal instanceof UserDetails userDetails)
                ? userRepo.findByUsername(userDetails.getUsername())
                        .map(User::getId)
                        .orElse(null)
                : null;
        op.setUserId(userId);

        return opRepo.save(op);
    }
}