package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * В качестве прям придирок: лучше распологать аннотации в порядке увеличения длинны
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAccountResponse {

    private Long id;
    private Boolean isActive;
    private Instant createdAt;
    private Instant modifiedAt;

}