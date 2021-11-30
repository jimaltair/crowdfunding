package ru.pcs.crowdfunding.tran.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.tran.repositories.PaymentsRepository;

@RequiredArgsConstructor
@Service
public class OperationServiceImpl implements OperationService {

    PaymentsRepository paymentsRepository;

    @Override
    public void createOperation(OperationDto operationDto) {
        //нужна валидация
        paymentsRepository.save(operationDto);
    }
}