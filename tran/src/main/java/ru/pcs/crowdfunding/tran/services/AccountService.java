package ru.pcs.crowdfunding.tran.services;

import ru.pcs.crowdfunding.tran.domain.Account;
import ru.pcs.crowdfunding.tran.dto.AccountDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

public interface AccountService {
    Long createAccount(Account account);
    BigDecimal getBalance(Account account, Instant dateTime);
    Optional<AccountDto> findById(Long id);

    Optional<AccountDto> updateAccount(Long id, AccountDto accountDto);

    AccountDto createNewAccount(AccountDto accountDto);

}
