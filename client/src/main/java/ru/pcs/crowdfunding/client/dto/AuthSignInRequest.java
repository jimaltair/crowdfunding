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
public class AuthSignInRequest {

    private String email;
    private String password;

}