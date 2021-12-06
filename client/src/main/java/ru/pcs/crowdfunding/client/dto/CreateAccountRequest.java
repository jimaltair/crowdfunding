package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAccountRequest {
    private Boolean isActive;
    private Instant createdAt;
    private Instant modifiedAt;

    public static CreateAccountRequest requestToCreateNewAccount() {
        return CreateAccountRequest.builder()
                .isActive(true)
                .createdAt(Instant.now())
                .modifiedAt(Instant.now())
                .build();
    }
}
