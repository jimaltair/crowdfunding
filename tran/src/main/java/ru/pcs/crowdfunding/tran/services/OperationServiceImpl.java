package ru.pcs.crowdfunding.tran.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.tran.domain.OperationType;
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
    private final OperationValidator operationValidator;

    @Override
    public OperationDto createOperation(OperationDto operationDto) {


        operationValidator.isValid(operationDto); // пока просто вызываю, ничего не обрабатываю; исключение должно проброситься сюда


        if (operationDto.getOperationType().equals(OperationType.Type.PAYMENT.toString()) ||
                operationDto.getOperationType().equals(OperationType.Type.REFUND.toString()) ||
                operationDto.getOperationType().equals(OperationType.Type.TOP_UP.toString())) {

            paymentsRepository.save(Payment.builder()
                    .account(accountsRepository.getById(operationDto.getDebitAccountId()))
                    .sum(operationDto.getSum())
                    .operation(operationsRepository.getById(operationDto.getId()))
                    .datetime(operationDto.getDatetime())
                    .build());

        }

        if (operationDto.getOperationType().equals(OperationType.Type.PAYMENT.toString()) ||
                operationDto.getOperationType().equals(OperationType.Type.REFUND.toString()) ||
                operationDto.getOperationType().equals(OperationType.Type.WITHDRAW.toString())) {

            paymentsRepository.save(Payment.builder()
                    .account(accountsRepository.getById(operationDto.getCreditAccountId()))
                    .sum(operationDto.getSum().multiply(BigDecimal.valueOf(-1)))
                    .operation(operationsRepository.getById(operationDto.getId()))
                    .datetime(operationDto.getDatetime())
                    .build());

        }
        return operationDto;
    }
}
