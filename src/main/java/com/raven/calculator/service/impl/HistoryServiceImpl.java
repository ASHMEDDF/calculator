package com.raven.calculator.service.impl;
import com.raven.calculator.config.exception.OperationNotFoundException;
import com.raven.calculator.dto.response.OperationResponse;
import com.raven.calculator.entity.Operation;
import com.raven.calculator.entity.OperationTypeEnum;
import com.raven.calculator.repository.OperationRepository;
import com.raven.calculator.repository.UserRepository;
import com.raven.calculator.service.HistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final OperationRepository opRepo;
    private final UserRepository userRepo;

    public HistoryServiceImpl(OperationRepository opRepo, UserRepository userRepo) {
        this.opRepo = opRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Page<OperationResponse> getHistory(
            String username,
            OperationTypeEnum operationType,
            Instant startDate,
            Instant endDate,
            Pageable pageable) {

        Long userId = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"))
                .getId();

        Page<Operation> page = opRepo.findByUserId(userId, pageable);

        return page.map(op -> new OperationResponse(
                op.getId(),
                op.getOperationType(),
                op.getOperandA(),
                op.getOperandB(),
                op.getResult(),
                op.getTimestamp(),
                op.getUserId()
        ));
    }

    @Override
    public Page<Operation> findHistory(Long userId,
                                       OperationTypeEnum type,
                                       Instant from,
                                       Instant to,
                                       Pageable pageable) {
        return opRepo.findHistoryFiltered(userId, type, from, to, pageable);
    }

    @Override
    public Long findUserIdByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username))
                .getId();
    }

    @Override
    public void deleteOperation(Long id) {

        boolean exists = opRepo.existsById(id);
        if (!exists) {
            throw new OperationNotFoundException(id);
        }
        opRepo.deleteById(id);
    }
}
