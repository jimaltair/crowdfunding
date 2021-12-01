package ru.pcs.crowdfunding.tran.services;

import ru.pcs.crowdfunding.tran.dto.OperationDto;

public interface OperationService {
    void createOperation(OperationDto operationDto);
}