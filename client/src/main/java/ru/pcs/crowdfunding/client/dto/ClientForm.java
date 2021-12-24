package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pcs.crowdfunding.client.domain.Client;
import ru.pcs.crowdfunding.client.domain.ClientImage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientForm {

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

    private String email;

    private BigDecimal sumAccount;

    private ClientImage image;


    public static ClientForm from(Client client) {
        return ClientForm.builder()
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .country(client.getCountry())
                .city(client.getCity())
                .image(client.getImage())
                .build();
    }
}
