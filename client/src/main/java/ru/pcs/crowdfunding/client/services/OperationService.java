package ru.pcs.crowdfunding.client.services;

import org.springframework.transaction.annotation.Transactional;
import ru.pcs.crowdfunding.client.dto.OperationDto;

import java.math.BigDecimal;

public interface OperationService {

    OperationDto operate(OperationDto operationDto) throws IllegalArgumentException;

    @Transactional
    void withdrawMoneyFromProject(Long projectId, Long clientId) throws IllegalArgumentException;

    @Transactional
    void paymentToProject(Long projectId, Long clientId, BigDecimal sumPayment) throws IllegalArgumentException;

    @Transactional
    void topUpToClient(Long clientId, BigDecimal sumTopUp) throws IllegalArgumentException;
}
