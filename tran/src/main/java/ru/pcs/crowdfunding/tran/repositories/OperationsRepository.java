package ru.pcs.crowdfunding.tran.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pcs.crowdfunding.tran.domain.Operation;

public interface OperationsRepository extends JpaRepository<Operation, Long> {
}
