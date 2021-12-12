package ru.pcs.crowdfunding.tran.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.pcs.crowdfunding.tran.domain.Account;

import java.util.Optional;

public interface AccountsRepository extends JpaRepository<Account, Long> {

    Optional<Account> findById(Long id);

    /**
     * Метод не используется... он нам нужен?
     */
    Page<Account> findAllByIsActive(Boolean isActive, Pageable pageable);

}