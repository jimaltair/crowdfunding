package ru.pcs.crowdfunding.client.dto;

import lombok.*;
import ru.pcs.crowdfunding.client.validation.NotSameNames;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NotSameNames
public class SignUpForm {

    @Size(min = 4, max = 20)
    @NotBlank
    private String firstName;

    @Size(min = 4, max = 20)
    @NotBlank
    private String lastName;

    @Size(min = 3, max = 20)
    @NotBlank
    private String country;

    @Size(min = 2, max = 20)
    @NotBlank
    private String city;

//    @NotBlank
//    @Size(min = 7, max = 20)
//    @ValidPassword
//    private String password;
//
//    @Email
//    @NotBlank
//    private String email;

}
