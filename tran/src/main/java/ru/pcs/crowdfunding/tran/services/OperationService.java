package ru.pcs.crowdfunding.tran.services;

import ru.pcs.crowdfunding.tran.dto.OperationDto;

import java.util.Optional;

public interface OperationService {

    OperationDto createOperation(OperationDto operationDto);

    Optional<OperationDto> findById(Long id);

}