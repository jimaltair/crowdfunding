package ru.pcs.crowdfunding.tran.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.tran.domain.Account;
import ru.pcs.crowdfunding.tran.dto.AccountDto;
import ru.pcs.crowdfunding.tran.repositories.AccountsRepository;
import ru.pcs.crowdfunding.tran.repositories.PaymentsRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

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

    @Override
    public Optional<AccountDto> findById(Long id) {
        Optional<Account> optionalAccount = accountsRepository.findById(id);
        return optionalAccount.map(AccountDto::from);
    }

    @Override
    public Optional<AccountDto> updateAccount(Long id, AccountDto accountDto) {
        Optional<Account> optionalAccount = accountsRepository.findById(id);
        if (!optionalAccount.isPresent()) {
            return Optional.empty();
        }

        Account account = optionalAccount.get();
        account.setCreatedAt(accountDto.getCreatedAt());
        account.setModifiedAt(accountDto.getModifiedAt());
        account.setIsActive(accountDto.getIsActive());
        accountsRepository.save(account);

        return Optional.of(AccountDto.from(account));
    }

    @Override
    public AccountDto createNewAccount(AccountDto accountDto) {
        Account account = Account.builder()
                .createdAt(accountDto.getCreatedAt())
                .modifiedAt(accountDto.getModifiedAt())
                .isActive(true)
                .build();
        accountsRepository.save(account);

        return AccountDto.from(account);
    }
}
