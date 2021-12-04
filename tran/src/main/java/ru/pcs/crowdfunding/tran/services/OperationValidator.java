package ru.pcs.crowdfunding.tran.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.tran.domain.OperationType;
import ru.pcs.crowdfunding.tran.dto.OperationDto;
import ru.pcs.crowdfunding.tran.repositories.AccountsRepository;
import ru.pcs.crowdfunding.tran.repositories.PaymentsRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class OperationValidator implements ConstraintValidator<ValidOperation, OperationDto> {

    private final AccountsRepository accountsRepository;
    private final PaymentsRepository paymentsRepository;

    @Override
    public boolean isValid(OperationDto operationDto, ConstraintValidatorContext constraintValidatorContext) {
        this.isOperationDtoNotNull(operationDto);
        this.isOperationTypeExist(operationDto);
        if (operationDto.getOperationType().equals(OperationType.Type.PAYMENT.toString()) ||
            operationDto.getOperationType().equals(OperationType.Type.REFUND.toString()) ||
            operationDto.getOperationType().equals(OperationType.Type.TOP_UP.toString())) {
            this.isDebitAccountIdExist(operationDto);
        }

        if (operationDto.getOperationType().equals(OperationType.Type.PAYMENT.toString()) ||
                operationDto.getOperationType().equals(OperationType.Type.REFUND.toString()) ||
                operationDto.getOperationType().equals(OperationType.Type.WITHDRAW.toString())) {
            this.isCreditAccountIdExist(operationDto);
        }
        this.isSumGreaterThanZero(operationDto);
        this.isBalanceEnoughForOperation(operationDto);
        return true;
    }


    private void isOperationDtoNotNull(OperationDto operationDto) {
        if (operationDto == null) {
            throw new IllegalArgumentException("Операция не передана / получен null");
        }
    }

    private void isOperationTypeExist(OperationDto operationDto) {
        if (!operationDto.getOperationType().equals(OperationType.Type.PAYMENT.toString())
                && !operationDto.getOperationType().equals(OperationType.Type.REFUND.toString())
                && !operationDto.getOperationType().equals(OperationType.Type.TOP_UP.toString())
                && !operationDto.getOperationType().equals(OperationType.Type.WITHDRAW.toString())) {
            throw new IllegalArgumentException("Типа операции " +  operationDto.getOperationType() + " не существует");
        }
    }

    private void isCreditAccountIdExist(OperationDto operationDto) {
        if (!accountsRepository.findById(operationDto.getCreditAccountId()).isPresent()) {
            throw new IllegalArgumentException("Счета credit_account_id = " + operationDto.getCreditAccountId() + " не существует");
        }
    }

    private void isDebitAccountIdExist(OperationDto operationDto) {
        if (!accountsRepository.findById(operationDto.getDebitAccountId()).isPresent()){
            throw new IllegalArgumentException("Счета debit_account_id = " + operationDto.getDebitAccountId() + " не существует");
        }
    }

    private void isSumGreaterThanZero(OperationDto operationDto) {
        if (operationDto.getSum().compareTo(BigDecimal.ZERO) < 1) {
            throw new IllegalArgumentException("Сумма = " + operationDto.getSum() + " операции меньше или равна нулю");
        }
    }

    private void isBalanceEnoughForOperation(OperationDto operationDto) {
        if (paymentsRepository.findBalanceByAccountAndDatetime(
                        accountsRepository.getById(operationDto.getCreditAccountId()),
                        operationDto.getDatetime())
                .subtract(operationDto.getSum()).compareTo(BigDecimal.ZERO) < 1) {
            throw new IllegalArgumentException("Для совершения операции на сумму " + operationDto.getSum() + " на счете недостаточно средств");
        }
    }

}