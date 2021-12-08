package ru.pcs.crowdfunding.client.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInForm {

    private Long id;

    private String email;

    private String password;


}
