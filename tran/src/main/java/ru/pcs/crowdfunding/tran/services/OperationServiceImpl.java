package ru.pcs.crowdfunding.tran.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.tran.dto.OperationDto;
import ru.pcs.crowdfunding.tran.repositories.PaymentsRepository;

import javax.validation.Valid;

@RequiredArgsConstructor
@Service
public class OperationServiceImpl implements OperationService {

    private final PaymentsRepository paymentsRepository;

    @Override
    public void createOperation(@Valid OperationDto operationDto) {
        paymentsRepository.saveAll(operationDto.getPayments());
    }
}