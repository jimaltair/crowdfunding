package ru.pcs.crowdfunding.tran.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pcs.crowdfunding.tran.domain.Account;

public interface AccountsRepository extends JpaRepository<Account, Long> {
}
