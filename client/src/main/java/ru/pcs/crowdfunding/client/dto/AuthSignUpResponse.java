package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthSignUpResponse {

    private Long userId;
    private String email;
    private String password;
    private String accessToken;
    private String refreshToken;
    private Boolean isActive;

}