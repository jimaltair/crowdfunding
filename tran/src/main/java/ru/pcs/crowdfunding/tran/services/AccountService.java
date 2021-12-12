package ru.pcs.crowdfunding.tran.services;

import ru.pcs.crowdfunding.tran.domain.Account;
import ru.pcs.crowdfunding.tran.dto.AccountDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

public interface AccountService {

    BigDecimal getBalance(Account account, Instant dateTime);

    Optional<Long> getContributorsCount(Long accountId, Instant dateTime);

    Optional<AccountDto> findById(Long id);

    Optional<AccountDto> updateAccount(Long id, AccountDto accountDto);

    AccountDto createAccount();

    Optional<AccountDto> deleteAccount(Long accountId);

}