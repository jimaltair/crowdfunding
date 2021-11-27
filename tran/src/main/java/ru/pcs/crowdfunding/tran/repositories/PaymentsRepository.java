package ru.pcs.crowdfunding.tran.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pcs.crowdfunding.tran.domain.Account;
import ru.pcs.crowdfunding.tran.domain.Payment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PaymentsRepository extends JpaRepository<Payment, Long> {

    @Query(value = "SELECT SUM(p.sum) FROM Payment p where p.account = ?1 and p.datetime <= ?2")
    BigDecimal findBalanceByAccountAndDatetime(Long account, Instant dateTime);

    Page<Payment> findPaymentsByDatetimeBetween(Instant dateBegin, Instant dateEnd, Pageable pageable);

    Page<Payment> findPaymentsByAccountAndDatetimeBetween(Account account, Instant datetime, Instant datetime2,
                                                          Pageable pageable);

    List<Payment> findPaymentsByOperationId(Long operationId);

    Optional<Payment> findById(Long id);


}
