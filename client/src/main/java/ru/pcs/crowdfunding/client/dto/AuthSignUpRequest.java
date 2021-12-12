package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthSignUpRequest {

    private Long userId;
    private String email;
    private String password;

}