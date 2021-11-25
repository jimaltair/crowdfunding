package ru.pcs.crowdfunding.tran.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pcs.crowdfunding.tran.domain.OperationType;

public interface OperationTypesRepository extends JpaRepository<OperationType, Long> {
}
