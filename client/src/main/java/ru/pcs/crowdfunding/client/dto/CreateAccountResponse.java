package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountResponse {

    private Long id;
    private Boolean isActive;
    private Instant createdAt;
    private Instant modifiedAt;

}