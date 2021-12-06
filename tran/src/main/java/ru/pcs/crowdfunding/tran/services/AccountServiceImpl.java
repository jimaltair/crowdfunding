package ru.pcs.crowdfunding.tran.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountsRepository accountsRepository;
    private final PaymentsRepository paymentsRepository;


    @Override
    public BigDecimal getBalance(Account account, Instant dateTime) {

        log.info("Запускается метод 'getBalance' с параметрами 'Account' - {} and 'Instant' - {}", account, dateTime);
        log.info("Попытка получаения баланса из 'paymentsRepository'");

        return paymentsRepository.findBalanceByAccountAndDatetime(account, dateTime);
    }

    @Override
    public Optional<AccountDto> findById(Long id) {

        log.info("Запускается метод 'findById' с параметром 'id' - {}", id);

        Optional<Account> optionalAccount = accountsRepository.findById(id);

        if (!optionalAccount.isPresent()) {
            log.warn("'Account' с id - {} не найден в 'accountsRepository'", id);
            return Optional.empty();
        }

        log.info("Получен 'Account' - {} из 'accountsRepository", optionalAccount);

        return optionalAccount.map(AccountDto::from);
    }

    @Override
    public Optional<AccountDto> updateAccount(Long id, AccountDto accountDto) {

        log.info("Запускается метод 'updateAccount' с параметрами 'id' - {} и 'AccountDto' - {}", id, accountDto);

        Optional<Account> optionalAccount = accountsRepository.findById(id);

        log.info("Получен 'Account' - {} из 'accountRepository", optionalAccount);

        if (!optionalAccount.isPresent()) {

            log.warn("'Account' с id - {} не найден в 'accountsRepository'", id);

            return Optional.empty();
        }

        Account account = optionalAccount.get();
        account.setCreatedAt(accountDto.getCreatedAt());
        account.setModifiedAt(accountDto.getModifiedAt());
        account.setIsActive(accountDto.getIsActive());

        log.info("Создан новый 'Account' - {} для обновления в 'accountsRepository'", account);

        accountsRepository.save(account);

        log.info("Сохранен 'Account' - {} в 'accountsRepository' ", account);

        return Optional.of(AccountDto.from(account));
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {

        log.info("Запускается метод 'createAccount' с параметром 'AccountDto' - {}", accountDto);

        Account account = Account.builder()
                .createdAt(accountDto.getCreatedAt())
                .modifiedAt(accountDto.getModifiedAt())
                .isActive(true)
                .build();

        log.info("Создан новый 'Account' - {} для сохранения в 'accountsRepository' ", account);

        accountsRepository.save(account);

        log.info("Сохранен 'Account' - {} в 'accountsRepository ", account);

        return AccountDto.from(account);
    }

    @Override
    public Optional<AccountDto> deleteAccount(Long accountId) {

        log.info("Запускается метод 'deleteAccount' с параметром 'id' - {}", accountId);

        Optional<Account> optionalAccount = accountsRepository.findById(accountId);

        log.info("Получен 'Account' - {} из 'accountsRepository'", optionalAccount);

        if (!optionalAccount.isPresent()) {

            log.warn("'Account' с id - {} не найден в 'accountsRepository'", accountId);

            return Optional.empty();
        }

        Account account = optionalAccount.get();
        account.setIsActive(false);
        account.setModifiedAt(Instant.now());

        log.info("Создан новый 'Account' - {} для деактивации в 'accountsRepository", account);

        accountsRepository.save(account);

        log.info("Деактивирован 'Account' - {} в 'accountsRepository ", account);

        return Optional.of(AccountDto.from(account));
    }
}
