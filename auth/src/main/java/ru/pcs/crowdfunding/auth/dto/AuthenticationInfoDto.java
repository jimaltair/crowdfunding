package ru.pcs.crowdfunding.auth.dto;

import lombok.*;
import ru.pcs.crowdfunding.auth.validation.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationInfoDto {

    @NotEmpty
    private Long userId;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 7, max = 20)
    @ValidPassword
    private String password;
}
