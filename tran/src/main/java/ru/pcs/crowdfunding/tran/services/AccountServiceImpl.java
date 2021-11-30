package ru.pcs.crowdfunding.tran.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.tran.domain.Account;
import ru.pcs.crowdfunding.tran.repositories.AccountsRepository;
import ru.pcs.crowdfunding.tran.repositories.PaymentsRepository;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountsRepository accountsRepository;
    private final PaymentsRepository paymentsRepository;

    @Override
    public Long createAccount(Account account) {
        accountsRepository.save(account);
        return account.getId();
    }

    @Override
    public BigDecimal getBalance(Account account, Instant dateTime) {
        return paymentsRepository.findBalanceByAccountAndDatetime(account, dateTime);
    }
}
