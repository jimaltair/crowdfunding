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
                    .account(accountsRepository.getById(operationDto.getCreditAccountId()))
                    .sum(operationDto.getSum())
                    .operation(operationsRepository.getById(operationDto.getId()))
                    .datetime(operationDto.getDatetime())
                    .build());

        }

        if (operationDto.getOperationType().equals("PAYMENT") ||
                operationDto.getOperationType().equals("REFUND") ||
                operationDto.getOperationType().equals("WITHDRAW")) {

            paymentsRepository.save(Payment.builder()
                    .account(accountsRepository.getById(operationDto.getDebitAccountId()))
                    .sum(operationDto.getSum())
                    .operation(operationsRepository.getById(operationDto.getId()))
                    .datetime(operationDto.getDatetime())
                    .build());

        }
        return operationDto;
    }

    public boolean isValid(OperationDto operationDto) {

        if (operationDto == null) {
            throw new IllegalArgumentException();
        }

        if (!operationDto.getOperationType().equals("PAYMENT")
            || !operationDto.getOperationType().equals("REFUND")
            || !operationDto.getOperationType().equals("TOP_UP")
            || !operationDto.getOperationType().equals("WITHDRAW")) {
            throw new IllegalArgumentException();
        }

        if (!operationsRepository.findById(operationDto.getId()).isPresent()) {
            throw new IllegalArgumentException();
        }

        if (!accountsRepository.findById(operationDto.getCreditAccountId()).isPresent()
            || !accountsRepository.findById(operationDto.getDebitAccountId()).isPresent()) {
            throw new IllegalArgumentException();
        }

        if (operationDto.getSum().compareTo(BigDecimal.ZERO) < 1) {
            throw new IllegalArgumentException();
        }

        if (paymentsRepository.findBalanceByAccountAndDatetime(
                accountsRepository.getById(operationDto.getDebitAccountId()),
                operationDto.getDatetime())
                .subtract(operationDto.getSum()).compareTo(BigDecimal.ZERO) < 1) {
            throw new IllegalArgumentException();
        }

        return true;
    }
}
