package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pcs.crowdfunding.client.domain.Client;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {
    private Long accountId;
}
