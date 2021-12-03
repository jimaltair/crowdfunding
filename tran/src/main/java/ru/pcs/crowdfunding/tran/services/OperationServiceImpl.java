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
    public void createOperation(OperationDto operationDto) {

        if (!this.isValid(operationDto)) {
            return;
        }

        if (operationDto.getOperationType().equals("PAYMENT")
            || operationDto.getOperationType().equals("REFUND")) {
            /*
            Сохраняем информацию о пополнении счета получателя
            */
            Payment recipientPayment = Payment.builder()
                    .account(accountsRepository.getById(operationDto.getCreditAccountId()))
                    .sum(operationDto.getSum())
                    .operation(operationsRepository.getById(operationDto.getId()))
                    .datetime(operationDto.getDatetime())
                    .build();
            paymentsRepository.save(recipientPayment);
            /*
            Сохраняем информацию о снятии средств со счета отправителя
            */
            Payment senderPayment = Payment.builder()
                    .account(accountsRepository.getById(operationDto.getDebitAccountId()))
                    .sum(operationDto.getSum().multiply(BigDecimal.valueOf(-1)))
                    .operation(operationsRepository.getById(operationDto.getId()))
                    .datetime(operationDto.getDatetime())
                    .build();
            paymentsRepository.save(senderPayment);

        } else if (operationDto.getOperationType().equals("TOP_UP")) {
            /*
            Сохраняем информацию о пополнении счета
             */
            Payment recipientPayment = Payment.builder()
                    .account(accountsRepository.getById(operationDto.getCreditAccountId()))
                    .sum(operationDto.getSum())
                    .operation(operationsRepository.getById(operationDto.getId()))
                    .datetime(operationDto.getDatetime())
                    .build();
            paymentsRepository.save(recipientPayment);
        } else if (operationDto.getOperationType().equals("WITHDRAW")) {
            /*
            Сохраняем информацию о снятии средств со счета
             */
            Payment senderPayment = Payment.builder()
                    .account(accountsRepository.getById(operationDto.getDebitAccountId()))
                    .sum(operationDto.getSum().multiply(BigDecimal.valueOf(-1)))
                    .operation(operationsRepository.getById(operationDto.getId()))
                    .datetime(operationDto.getDatetime())
                    .build();
            paymentsRepository.save(senderPayment);
        }
    }

    boolean isValid(OperationDto operationDto) {

        if (operationDto == null) {
            return false;
        }

        if (!operationDto.getOperationType().equals("PAYMENT")
            || !operationDto.getOperationType().equals("REFUND")
            || !operationDto.getOperationType().equals("TOP_UP")
            || !operationDto.getOperationType().equals("WITHDRAW")) {
            return false;
        }

        if (!operationsRepository.findById(operationDto.getId()).isPresent()) {
            return false;
        }

        if (!accountsRepository.findById(operationDto.getCreditAccountId()).isPresent()
            || !accountsRepository.findById(operationDto.getDebitAccountId()).isPresent()) {
            return false;
        }

        if (operationDto.getSum().compareTo(BigDecimal.ZERO) < 1) {
            return false;
        }

        return true;
    }
}
