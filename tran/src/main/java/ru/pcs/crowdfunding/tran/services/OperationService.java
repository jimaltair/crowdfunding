package ru.pcs.crowdfunding.tran.services;

import ru.pcs.crowdfunding.tran.domain.Operation;
import ru.pcs.crowdfunding.tran.dto.OperationDto;

public interface OperationService {
    OperationDto createOperation(OperationDto operationDto);
}