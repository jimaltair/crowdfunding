package ru.pcs.crowdfunding.tran.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.tran.domain.Account;
import ru.pcs.crowdfunding.tran.dto.AccountDto;
import ru.pcs.crowdfunding.tran.repositories.AccountsRepository;
import ru.pcs.crowdfunding.tran.repositories.OperationsRepository;
import ru.pcs.crowdfunding.tran.repositories.PaymentsRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountsRepository accountsRepository;
    private final PaymentsRepository paymentsRepository;
    private final OperationsRepository operationsRepository;

    @Override
    public BigDecimal getBalance(Account account, Instant dateTime) {
        BigDecimal balance = paymentsRepository.findBalanceByAccountAndDatetime(account, dateTime);
        log.info("for account = {} get balance = {} ", account.getId(), balance);
        return balance;
    }

    @Override
    public Optional<Long> getContributorsCount(Long accountId, Instant dateTime) {
        return accountsRepository.findById(accountId)
                .map(account -> operationsRepository.getContributorsCount(account, dateTime));
    }

    @Override
    public Optional<AccountDto> findById(Long id) {
        Optional<Account> optionalAccount = accountsRepository.findById(id);
        Optional<AccountDto> accountDto = optionalAccount.map(AccountDto::from);
        log.info("find account = {}", accountDto);
        return accountDto;
    }

    /**
     * Возможно тут имеет смысл использовать @Retryable и @Transactional
     */
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
        account = accountsRepository.save(account);

        Optional<AccountDto> updateAccount = Optional.of(AccountDto.from(account));
        log.info("update account = {}", updateAccount);
        return updateAccount;
    }

    /** Возможно тут имеет смысл использовать @Retryable и @Transactional*/
    @Override
    public AccountDto createAccount() {
        Account account = Account.builder()
                .createdAt(Instant.now())
                .modifiedAt(Instant.now())
                .isActive(true)
                .build();
        account = accountsRepository.save(account);
        AccountDto createAccount = AccountDto.from(account);
        log.info("create account = {}", createAccount);
        return createAccount;
    }

    /** Возможно тут имеет смысл использовать @Retryable и @Transactional*/
    @Override
    public Optional<AccountDto> deleteAccount(Long accountId) {
        Optional<Account> optionalAccount = accountsRepository.findById(accountId);

        if (!optionalAccount.isPresent()) {
            return Optional.empty();
        }
        Account account = optionalAccount.get();
        account.setIsActive(false);
        account.setModifiedAt(Instant.now());
        account = accountsRepository.save(account);

        Optional<AccountDto> deleteAccount = Optional.of(AccountDto.from(account));
        log.info("delete account = {}", deleteAccount);
        return deleteAccount;
    }

}