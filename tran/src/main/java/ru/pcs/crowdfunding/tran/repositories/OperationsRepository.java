package ru.pcs.crowdfunding.tran.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pcs.crowdfunding.tran.domain.Account;
import ru.pcs.crowdfunding.tran.domain.Operation;
import ru.pcs.crowdfunding.tran.domain.OperationType;

import java.time.Instant;
import java.util.Optional;

public interface OperationsRepository extends JpaRepository<Operation, Long> {

    Optional<Operation> findById(Long id);

    Page<Operation> findAllByInitiator(Long initiator, Pageable pageable);

    Page<Operation> findAllByOperationType(OperationType operationType, Pageable pageable);

    Page<Operation> findAllByDebitAccount(Account account, Pageable pageable);

    Page<Operation> findAllByCreditAccount(Account account, Pageable pageable);

    default Long getContributorsCount(Account account, Instant dateTime) {
        return getContributorsCountWithOperationType(account, dateTime, OperationType.Type.PAYMENT);
    }

    @Query(value = "SELECT COUNT(DISTINCT o.creditAccount) " +
                   "FROM Operation o " +
                   "WHERE o.debitAccount = ?1 AND o.datetime <= ?2 AND o.operationType.type = ?3")
    Long getContributorsCountWithOperationType(Account account, Instant dateTime, OperationType.Type type);
}
