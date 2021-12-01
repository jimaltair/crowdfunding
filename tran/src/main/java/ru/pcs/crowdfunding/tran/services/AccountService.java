package ru.pcs.crowdfunding.tran.services;

import ru.pcs.crowdfunding.tran.domain.Account;

import java.math.BigDecimal;
import java.time.Instant;

public interface AccountService {
    Long createAccount(Account account);
    BigDecimal getBalance(Account account, Instant dateTime);
}
