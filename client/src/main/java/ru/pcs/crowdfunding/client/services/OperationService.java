package ru.pcs.crowdfunding.client.services;

import ru.pcs.crowdfunding.client.dto.OperationDto;

public interface OperationService {

    OperationDto operate(OperationDto operationDto) throws IllegalArgumentException;
}
