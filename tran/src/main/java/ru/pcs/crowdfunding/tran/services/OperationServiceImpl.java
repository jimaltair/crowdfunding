package ru.pcs.crowdfunding.tran.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
        log.info("Запущен 'operationBuilder' с параметром 'operationDto' - {}", operationDto);
        Operation operation = Operation.builder()
            .initiator(operationDto.getInitiatorId())
            .datetime(operationDto.getDatetime())
            .sum(operationDto.getSum())
            .operationType(operationTypesRepository.findOperationType(operationDto.getOperationType()))
            .build();
        log.info("Результат выполнения 'operationBuilder' - {}", operation);
        return operation;
    }

    private Payment enrollBuilder(OperationDto operationDto, Operation operation) {
        log.info("Запускается метод 'enrollBuilder' с параметрами 'operationDto' - {} , 'operation' - {}"
                , operationDto, operation);

        Payment result = Payment.builder()
                .account(accountsRepository.getById(operationDto.getDebitAccountId()))
                .sum(operationDto.getSum())
                .operation(operation)
                .datetime(operationDto.getDatetime())
                .build();
        log.info("Результат выполнения метода 'enrollBuilder' - {} с запуском 'accountsRepository.getById({})'"
        , result, operationDto.getDebitAccountId());

        return result;
    }

    private Payment writeOffBuilder(OperationDto operationDto, Operation operation) {
        log.info("Запускается метод 'writeOffBuilder' с параметрами 'operationDto' - {} , 'operation' - {}"
                , operationDto, operation);

        Payment result = Payment.builder()
                .account(accountsRepository.getById(operationDto.getCreditAccountId()))
                .sum(operationDto.getSum().multiply(BigDecimal.valueOf(-1)))
                .operation(operation)
                .datetime(operationDto.getDatetime())
                .build();
        log.info("Результат выполнения метода 'writeOffBuilder' - {} с запуском 'accountsRepository.getById({})'"
        , result, operationDto.getCreditAccountId());

        return result;
    }

    private void payment(OperationDto operationDto, Operation operation) {
        log.info("Запускается метод 'payment' с параметрами 'operationDto' - {} , 'operation' - {}"
                , operationDto, operation);
        log.info("Запускается метод 'save' в 'paymentsRepository' c параметром 'enrollBuilder(operationDto, operation)' - {}",
                enrollBuilder(operationDto, operation));
        paymentsRepository.save(enrollBuilder(operationDto, operation)); //зачисление

        log.info("Запускается метод 'save' в 'paymentsRepository' c параметром 'writeOffBuilder(operationDto, operation)' - {}",
                writeOffBuilder(operationDto, operation));
        paymentsRepository.save(writeOffBuilder(operationDto, operation)); //списание
    }

    private void refund(OperationDto operationDto, Operation operation) {
        log.info("Запускается метод 'refund' с параметрами 'operationDto' - {} , 'operation' - {}"
                , operationDto, operation);
        log.info("Запускается метод 'save' в 'paymentsRepository' c параметром 'writeOffBuilder(operationDto, operation)' - {}",
                writeOffBuilder(operationDto, operation));
        paymentsRepository.save(writeOffBuilder(operationDto, operation)); //списание

        log.info("Запускается метод 'save' в 'paymentsRepository' c параметром 'enrollBuilder(operationDto, operation)' - {}",
                enrollBuilder(operationDto, operation));
        paymentsRepository.save(enrollBuilder(operationDto, operation)); //зачисление
    }

    private void topUp(OperationDto operationDto, Operation operation) {
        log.info("Запускается метод 'topUp' с параметрами 'operationDto' - {} , 'operation' - {}"
                , operationDto, operation);
        log.info("Запускается метод 'save' в 'paymentsRepository' c параметром 'enrollBuilder(operationDto, operation)' - {}",
                enrollBuilder(operationDto, operation));
        paymentsRepository.save(enrollBuilder(operationDto, operation)); //зачисление
    }

    private void withdraw(OperationDto operationDto, Operation operation) {
        log.info("Запускается метод 'withdraw' с параметрами 'operationDto' - {} , 'operation' - {}"
                , operationDto, operation);
        log.info("Запускается метод 'save' в 'paymentsRepository' c параметром 'writeOffBuilder(operationDto, operation)' - {}",
                writeOffBuilder(operationDto, operation));
        paymentsRepository.save(writeOffBuilder(operationDto, operation)); //зачисление
    }
    //endregion

    @Override
    public OperationDto createOperation(OperationDto operationDto) { //TODO подумать над транзакционностью
        log.info("Запускается метод 'createOperation' с параметром 'operationDto' - {}", operationDto);

        operationValidator.isValid(operationDto);
        String operationType = operationDto.getOperationType();

        Operation build =  operationBuilder(operationDto);
        log.info("Создан 'build' - {}", build);
        Operation operation;


        if (operationType.equals(OperationType.Type.PAYMENT.toString())) {
            log.info("Выполнено условие 'PAYMENT' для 'operationDto'");
            build.setDebitAccount(accountsRepository.getById(operationDto.getDebitAccountId()));
            build.setCreditAccount(accountsRepository.getById(operationDto.getCreditAccountId()));
            log.info("Заменены параметры 'debitAccount', 'creditAccount' в 'build' - {} с запуском 'accountsRepository'", build);
            operation = operationsRepository.save(build);
            log.info("Создан 'Operation'  - {} с запуском 'operationsRepository'", operation);
            payment(operationDto, operation);
        }

        if (operationType.equals(OperationType.Type.REFUND.toString())) {
            log.info("Выполнено условие 'REFUND' для 'operationDto'");
            build.setDebitAccount(accountsRepository.getById(operationDto.getDebitAccountId()));
            build.setCreditAccount(accountsRepository.getById(operationDto.getCreditAccountId()));
            log.info("Заменены параметры 'debitAccount', 'creditAccount' в 'build' - {} с запуском 'accountsRepository'", build);
            operation = operationsRepository.save(build);
            log.info("Создан 'Operation'  - {} с запуском 'operationsRepository'", operation);
            refund(operationDto, operation);
        }

        if (operationType.equals(OperationType.Type.TOP_UP.toString())) {
            log.info("Выполнено условие 'TOP_UP' для 'operationDto'");
            build.setDebitAccount(accountsRepository.getById(operationDto.getDebitAccountId()));
            build.setCreditAccount(accountsRepository.getById(1L));
            log.info("Заменены параметры 'debitAccount', 'creditAccount' в 'build' - {} с запуском 'accountsRepository'", build);
            operation = operationsRepository.save(build);
            log.info("Создан 'Operation'  - {} с запуском 'operationsRepository'", operation);
            topUp(operationDto, operation);
        }

        if (operationType.equals(OperationType.Type.WITHDRAW.toString())) {
            log.info("Выполнено условие 'TOP_UP' для 'operationDto'");
            build.setDebitAccount(accountsRepository.getById(1L));
            build.setCreditAccount(accountsRepository.getById(operationDto.getCreditAccountId()));
            log.info("Заменены параметры 'debitAccount', 'creditAccount' в 'build' - {} с запуском 'accountsRepository'", build);
            operation = operationsRepository.save(build);
            log.info("Создан 'Operation'  - {} с запуском 'operationsRepository'", operation);
            withdraw(operationDto, operation);
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

