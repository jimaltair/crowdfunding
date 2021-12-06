package ru.pcs.crowdfunding.tran.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class OperationServiceImpl implements OperationService {

    private final PaymentsRepository paymentsRepository;
    private final AccountsRepository accountsRepository;
    private final OperationsRepository operationsRepository;
    private final OperationTypesRepository operationTypesRepository;
    private final OperationValidator operationValidator;

    //region private methods
    private Operation operationBuilder(OperationDto operationDto) {
        log.info("Запускается метод 'operationBuilder' с параметром 'operationDto' - {}", operationDto);
        Operation operation = Operation.builder()
            .initiator(operationDto.getInitiatorId())
            .datetime(operationDto.getDatetime())
            .sum(operationDto.getSum())
            .operationType(operationTypesRepository.findOperationType(operationDto.getOperationType()))
            .build();
        log.info("Результат 'operationBuilder' - {}", operation);
        return operation;
    }

    private Payment replenishmentTransactionBuilder(OperationDto operationDto, Operation operation) {
        log.info("Запускается метод 'replenishmentTransactionBuilder' с параметрами 'operationDto' - {} , 'operation' - {}"
                , operationDto, operation);
        Payment result = Payment.builder()
                .account(accountsRepository.getById(operationDto.getDebitAccountId()))
                .sum(operationDto.getSum())
                .operation(operation)
                .datetime(operationDto.getDatetime())
                .build();
        log.info("Результат 'replenishmentTransactionBuilder' - {}", result);
        return result;
    }

    private Payment writeOffTransactionBuilder(OperationDto operationDto, Operation operation) {
        log.info("Запускается метод 'writeOffTransactionBuilder' с параметрами 'operationDto' - {}, 'operation' - {}"
                , operationDto, operation);
        Payment result = Payment.builder()
                .account(accountsRepository.getById(operationDto.getCreditAccountId()))
                .sum(operationDto.getSum().multiply(BigDecimal.valueOf(-1)))
                .operation(operation)
                .datetime(operationDto.getDatetime())
                .build();
        log.info("Результат 'writeOffTransactionBuilder' - {}", result);
        return result;
    }
    //endregion

    @Override
    @Transactional
    public OperationDto createOperation(OperationDto operationDto) throws IllegalArgumentException {
        log.info("Запускается метод 'createOperation' с параметром 'operationDto' - {}", operationDto);

        operationValidator.isValid(operationDto);
        String operationType = operationDto.getOperationType();

        Operation operationBuild =  operationBuilder(operationDto);
        Operation operation;
        log.info("Созданы 'operationBuild' - {} , пустой 'operation'", operationBuild);

        if (operationType.equals(OperationType.Type.REFUND.toString()) ||
            operationType.equals(OperationType.Type.PAYMENT.toString())
        ) {
            log.info("Выполнено условие 'REFUND' или 'PAYMENT' для 'operationDto'");
            operationBuild.setDebitAccount(accountsRepository.getById(operationDto.getDebitAccountId()));
            operationBuild.setCreditAccount(accountsRepository.getById(operationDto.getCreditAccountId()));
            log.info("Изменены поля 'debitAccount', 'creditAccount' для 'operationBuild' - {}", operationBuild);
            operation = operationsRepository.save(operationBuild);
            log.info("Результат изменения 'operation' с вызвовом метода 'save' в 'operationsRepository' - {}", operation);
            paymentsRepository.save(writeOffTransactionBuilder(operationDto, operation));
            paymentsRepository.save(replenishmentTransactionBuilder(operationDto, operation));
            log.info("Результат 'operationDto' - {}, 'operation' - {}" +
                    " после вызовов методов 'writeOffTransactionBuilder', 'replenishmentTransactionBuilder'", operationDto, operation);
        }

        if (operationType.equals(OperationType.Type.TOP_UP.toString())) {
            log.info("Выполнено условие 'TOP_UP' для 'operationDto'");
            operationBuild.setDebitAccount(accountsRepository.getById(operationDto.getDebitAccountId()));
            operationBuild.setCreditAccount(accountsRepository.getById(1L));
            log.info("Изменены поля 'debitAccount', 'creditAccount' для 'operationBuild' - {}", operationBuild);
            operation = operationsRepository.save(operationBuild);
            log.info("Результат изменения 'operation' с вызвовом метода 'save' в 'operationsRepository' - {}", operation);
            paymentsRepository.save(replenishmentTransactionBuilder(operationDto, operation));
            log.info("Результат 'operationDto' - {}, 'operation' - {}" +
                    " после вызовов методов 'replenishmentTransactionBuilder'", operationDto, operation);
        }

        if (operationType.equals(OperationType.Type.WITHDRAW.toString())) {
            log.info("Выполнено условие 'WITHDRAW' для 'operationDto'");
            operationBuild.setDebitAccount(accountsRepository.getById(1L));
            operationBuild.setCreditAccount(accountsRepository.getById(operationDto.getCreditAccountId()));
            log.info("Изменены поля 'debitAccount', 'creditAccount' для 'operationBuild' - {}", operationBuild);
            operation = operationsRepository.save(operationBuild);
            log.info("Результат изменения 'operation' с вызвовом метода 'save' в 'operationsRepository' - {}", operation);
            paymentsRepository.save(writeOffTransactionBuilder(operationDto, operation));
            log.info("Результат 'operationDto' - {}, 'operation' - {}" +
                    " после вызовов методов 'writeOffTransactionBuilder'", operationDto, operation);
        }
            return operationDto;
    }

    @Override
    public Optional<OperationDto> findById(Long id) {
        log.info("Запускается метод 'findById' с параметром 'id' - {}", id);
        Optional<Operation> optionalOperation = operationsRepository.findById(id);
        log.info("Результат выполнения метода 'findById' - {}", optionalOperation.map(OperationDto::from));
        return optionalOperation.map(OperationDto::from);
    }
}

