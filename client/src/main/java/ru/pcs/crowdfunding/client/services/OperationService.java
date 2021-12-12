package ru.pcs.crowdfunding.client.services;

import ru.pcs.crowdfunding.client.dto.OperationDto;

public interface OperationService {

    /**
     * есть предложение использовать над каждым методом @Retryable(value = Exception.class)
     */
    OperationDto operate(OperationDto operationDto) throws IllegalArgumentException;

}