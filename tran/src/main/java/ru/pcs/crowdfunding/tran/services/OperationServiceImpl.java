package ru.pcs.crowdfunding.tran.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.tran.domain.Payment;
import ru.pcs.crowdfunding.tran.dto.OperationDto;
import ru.pcs.crowdfunding.tran.repositories.AccountsRepository;
import ru.pcs.crowdfunding.tran.repositories.OperationsRepository;
import ru.pcs.crowdfunding.tran.repositories.PaymentsRepository;

import javax.validation.Valid;

@RequiredArgsConstructor
@Service
public class OperationServiceImpl implements OperationService {

    private final PaymentsRepository paymentsRepository;
    private final AccountsRepository accountsRepository;
    private final OperationsRepository operationsRepository;

    @Override
    public void createOperation(OperationDto operationDto) {

        if (this.isValid(operationDto)) {
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
                    .sum(operationDto.getSum())
                    .operation(operationsRepository.getById(operationDto.getId()))
                    .datetime(operationDto.getDatetime())
                    .build();
            paymentsRepository.save(senderPayment);
        }
    }

    boolean isValid(OperationDto operationDto) {
       return false;
    }
}
