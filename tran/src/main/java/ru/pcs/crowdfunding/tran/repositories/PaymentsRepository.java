package ru.pcs.crowdfunding.tran.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pcs.crowdfunding.tran.domain.Payment;

public interface PaymentsRepository extends JpaRepository<Payment, Long> {
}
