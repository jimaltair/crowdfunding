package ru.pcs.crowdfunding.client.dto;

import lombok.*;
import ru.pcs.crowdfunding.client.domain.Client;
import ru.pcs.crowdfunding.client.validation.NotSameNames;
import ru.pcs.crowdfunding.client.validation.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NotSameNames
public class SignUpForm {

    private Long id;

    @Size(min = 2, max = 20)
    @NotBlank
    private String firstName;

    @Size(min = 2, max = 20)
    @NotBlank
    private String lastName;

    @Size(min = 2, max = 20)
    @NotBlank
    private String country;

    @Size(min = 2, max = 20)
    @NotBlank
    private String city;

    @NotBlank
    @Size(min = 7, max = 20)
    @ValidPassword
    private String password;

    @Email
    @NotBlank
    @Size(min = 4, max = 45)
    private String email;

    public static SignUpForm from(Client client) {
        return SignUpForm.builder()
                .id(client.getId())
                .city(client.getCity())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .country(client.getCountry())
                .build();
    }

}
