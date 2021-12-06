package ru.pcs.crowdfunding.tran.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pcs.crowdfunding.tran.domain.Operation;
import ru.pcs.crowdfunding.tran.domain.OperationType;
import ru.pcs.crowdfunding.tran.domain.Payment;
import ru.pcs.crowdfunding.tran.dto.OperationDto;
import ru.pcs.crowdfunding.tran.repositories.AccountsRepository;
import ru.pcs.crowdfunding.tran.repositories.OperationTypesRepository;
import ru.pcs.crowdfunding.tran.repositories.OperationsRepository;
import ru.pcs.crowdfunding.tran.repositories.PaymentsRepository;
import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OperationServiceImpl implements OperationService {

    private final PaymentsRepository paymentsRepository;
    private final AccountsRepository accountsRepository;
    private final OperationsRepository operationsRepository;
    private final OperationTypesRepository operationTypesRepository;
    private final OperationValidator operationValidator;

    //region private methods
    private Operation operationBuilder(OperationDto operationDto) {
        Operation operation = Operation.builder()
            .initiator(operationDto.getInitiatorId())
            .datetime(operationDto.getDatetime())
            .sum(operationDto.getSum())
            .operationType(operationTypesRepository.findOperationType(operationDto.getOperationType()))
            .build();
        return operation;
    }

    private Payment replenishmentTransactionBuilder(OperationDto operationDto, Operation operation) {
        return Payment.builder()
            .account(accountsRepository.getById(operationDto.getDebitAccountId()))
            .sum(operationDto.getSum())
            .operation(operation)
            .datetime(operationDto.getDatetime())
            .build();
    }

    private Payment writeOffTransactionBuilder(OperationDto operationDto, Operation operation) {
        return Payment.builder()
            .account(accountsRepository.getById(operationDto.getCreditAccountId()))
            .sum(operationDto.getSum().multiply(BigDecimal.valueOf(-1)))
            .operation(operation)
            .datetime(operationDto.getDatetime())
            .build();
    }
    //endregion

    @Override
    @Transactional
    public OperationDto createOperation(OperationDto operationDto) throws IllegalArgumentException {

        operationValidator.isValid(operationDto);
        String operationType = operationDto.getOperationType();

        Operation operationBuild =  operationBuilder(operationDto);
        Operation operation;

        if (operationType.equals(OperationType.Type.REFUND.toString()) ||
            operationType.equals(OperationType.Type.PAYMENT.toString())
        ) {
            operationBuild.setDebitAccount(accountsRepository.getById(operationDto.getDebitAccountId()));
            operationBuild.setCreditAccount(accountsRepository.getById(operationDto.getCreditAccountId()));
            operation = operationsRepository.save(operationBuild);
            paymentsRepository.save(writeOffTransactionBuilder(operationDto, operation));
            paymentsRepository.save(replenishmentTransactionBuilder(operationDto, operation));
        }

        if (operationType.equals(OperationType.Type.TOP_UP.toString())) {
            operationBuild.setDebitAccount(accountsRepository.getById(operationDto.getDebitAccountId()));
            operationBuild.setCreditAccount(accountsRepository.getById(1L));
            operation = operationsRepository.save(operationBuild);
            paymentsRepository.save(replenishmentTransactionBuilder(operationDto, operation));
        }

        if (operationType.equals(OperationType.Type.WITHDRAW.toString())) {
            operationBuild.setDebitAccount(accountsRepository.getById(1L));
            operationBuild.setCreditAccount(accountsRepository.getById(operationDto.getCreditAccountId()));
            operation = operationsRepository.save(operationBuild);
            paymentsRepository.save(writeOffTransactionBuilder(operationDto, operation));
        }
            return operationDto;
    }

    @Override
    public Optional<OperationDto> findById(Long id) {
        Optional<Operation> optionalOperation = operationsRepository.findById(id);
        return optionalOperation.map(OperationDto::from);
    }
}
