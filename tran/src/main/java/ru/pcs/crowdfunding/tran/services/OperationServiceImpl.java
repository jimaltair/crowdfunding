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

    private Payment enrollBuilder(OperationDto operationDto, Operation operation) {
        return Payment.builder()
            .account(accountsRepository.getById(operationDto.getDebitAccountId()))
            .sum(operationDto.getSum())
            .operation(operation)
            .datetime(operationDto.getDatetime())
            .build();
    }

    private Payment writeOffBuilder(OperationDto operationDto, Operation operation) {
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

        Operation build =  operationBuilder(operationDto);
        Operation operation;


        if (operationType.equals(OperationType.Type.PAYMENT.toString())) {
            build.setDebitAccount(accountsRepository.getById(operationDto.getDebitAccountId()));
            build.setCreditAccount(accountsRepository.getById(operationDto.getCreditAccountId()));
            operation = operationsRepository.save(build);
            paymentsRepository.save(enrollBuilder(operationDto, operation)); //зачисление
            paymentsRepository.save(writeOffBuilder(operationDto, operation)); //списание
        }

        if (operationType.equals(OperationType.Type.REFUND.toString())) {
            build.setDebitAccount(accountsRepository.getById(operationDto.getDebitAccountId()));
            build.setCreditAccount(accountsRepository.getById(operationDto.getCreditAccountId()));
            operation = operationsRepository.save(build);
            paymentsRepository.save(writeOffBuilder(operationDto, operation)); //списание
             paymentsRepository.save(enrollBuilder(operationDto, operation)); //зачисление
        }

        if (operationType.equals(OperationType.Type.TOP_UP.toString())) {
            build.setDebitAccount(accountsRepository.getById(operationDto.getDebitAccountId()));
            build.setCreditAccount(accountsRepository.getById(1L));
            operation = operationsRepository.save(build);
            paymentsRepository.save(enrollBuilder(operationDto, operation)); //зачисление
        }

        if (operationType.equals(OperationType.Type.WITHDRAW.toString())) {
            build.setDebitAccount(accountsRepository.getById(1L));
            build.setCreditAccount(accountsRepository.getById(operationDto.getCreditAccountId()));
            operation = operationsRepository.save(build);
            paymentsRepository.save(writeOffBuilder(operationDto, operation)); //списание
        }
            return operationDto;
    }

    @Override
    public Optional<OperationDto> findById(Long id) {
        Optional<Operation> optionalOperation = operationsRepository.findById(id);
        return optionalOperation.map(OperationDto::from);
    }
}

