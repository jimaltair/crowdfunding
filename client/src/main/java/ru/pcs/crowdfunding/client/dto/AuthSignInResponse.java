package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * В качестве прям придирок: лучше распологать аннотации в порядке увеличения длинны
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthSignInResponse {

    private Long userId;
    private String email;
    private String password;
    private String accessToken;
    private String refreshToken;
    private Boolean isActive;

}