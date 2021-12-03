package ru.pcs.crowdfunding.tran.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.tran.domain.Payment;
import ru.pcs.crowdfunding.tran.dto.OperationDto;
import ru.pcs.crowdfunding.tran.repositories.AccountsRepository;
import ru.pcs.crowdfunding.tran.repositories.OperationsRepository;
import ru.pcs.crowdfunding.tran.repositories.PaymentsRepository;
import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class OperationServiceImpl implements OperationService {

    private final PaymentsRepository paymentsRepository;
    private final AccountsRepository accountsRepository;
    private final OperationsRepository operationsRepository;

    @Override
    public OperationDto createOperation(OperationDto operationDto) {

        this.isValid(operationDto);

        if (operationDto.getOperationType().equals("PAYMENT") ||
                operationDto.getOperationType().equals("REFUND") ||
                operationDto.getOperationType().equals("TOP_UP")) {

            paymentsRepository.save(Payment.builder()
                    .account(accountsRepository.getById(operationDto.getDebitAccountId()))
                    .sum(operationDto.getSum())
                    .operation(operationsRepository.getById(operationDto.getId()))
                    .datetime(operationDto.getDatetime())
                    .build());

        }

        if (operationDto.getOperationType().equals("PAYMENT") ||
                operationDto.getOperationType().equals("REFUND") ||
                operationDto.getOperationType().equals("WITHDRAW")) {

            paymentsRepository.save(Payment.builder()
                    .account(accountsRepository.getById(operationDto.getCreditAccountId()))
                    .sum(operationDto.getSum().multiply(BigDecimal.valueOf(-1)))
                    .operation(operationsRepository.getById(operationDto.getId()))
                    .datetime(operationDto.getDatetime())
                    .build());

        }
        return operationDto;
    }

    public boolean isValid(OperationDto operationDto) {

        if (operationDto == null) {
            throw new IllegalArgumentException("Информация не передана");
        }

        if (!operationDto.getOperationType().equals("PAYMENT")
            || !operationDto.getOperationType().equals("REFUND")
            || !operationDto.getOperationType().equals("TOP_UP")
            || !operationDto.getOperationType().equals("WITHDRAW")) {
            throw new IllegalArgumentException("Такого типа операции не существует");
        }

        if (!operationsRepository.findById(operationDto.getId()).isPresent()) {
            throw new IllegalArgumentException("Такой операции не существует");
        }

        if (!accountsRepository.findById(operationDto.getCreditAccountId()).isPresent()
            || !accountsRepository.findById(operationDto.getDebitAccountId()).isPresent()) {
            throw new IllegalArgumentException("Данного счета не существует");
        }

        if (operationDto.getSum().compareTo(BigDecimal.ZERO) < 1) {
            throw new IllegalArgumentException("Сумма операции меньше или равна нулю");
        }

        if (paymentsRepository.findBalanceByAccountAndDatetime(
                accountsRepository.getById(operationDto.getCreditAccountId()),
                operationDto.getDatetime())
                .subtract(operationDto.getSum()).compareTo(BigDecimal.ZERO) < 1) {
            throw new IllegalArgumentException("Недостаточно средств на счете");
        }

        return true;
    }
}
