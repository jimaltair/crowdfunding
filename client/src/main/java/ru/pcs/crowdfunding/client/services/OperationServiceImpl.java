package ru.pcs.crowdfunding.client.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.client.api.TransactionServiceClient;
import ru.pcs.crowdfunding.client.dto.OperationDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {

    private final TransactionServiceClient transactionServiceClient;

    @Override
    /** А мы не хотим провести эту операцию транзакционно? */
    public OperationDto operate(OperationDto operationDto) throws IllegalArgumentException {
        /** А мы не хотим отвалидировать сначала входные данные? */
        return transactionServiceClient.operate(operationDto);
    }

}