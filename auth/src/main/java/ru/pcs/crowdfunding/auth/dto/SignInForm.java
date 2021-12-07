package ru.pcs.crowdfunding.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignInForm {

    private Long userId;
    private String password;
    private String email;
    private String role;
    private String status;
}
