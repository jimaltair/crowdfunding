package ru.pcs.crowdfunding.client.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.client.api.TransactionServiceClient;
import ru.pcs.crowdfunding.client.dto.OperationDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class OperationServiceImpl implements OperationService {

    private final TransactionServiceClient transactionServiceClient;

    @Override
    public OperationDto operate(OperationDto operationDto) throws IllegalArgumentException {
        return transactionServiceClient.operate(operationDto);
    }
}
