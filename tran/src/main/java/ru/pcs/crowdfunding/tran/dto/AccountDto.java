package ru.pcs.crowdfunding.tran.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pcs.crowdfunding.tran.domain.Account;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    private Long id;
    private Boolean isActive;
    private Instant createdAt;
    private Instant modifiedAt;

    public static AccountDto from(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .isActive(account.getIsActive())
                .createdAt(account.getCreatedAt())
                .modifiedAt(account.getModifiedAt())
                .build();
    }

    public static List<AccountDto> from(List<Account> accounts) {
        return accounts.stream()
                .map(AccountDto::from)
                .collect(Collectors.toList());
    }

}