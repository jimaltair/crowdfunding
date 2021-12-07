package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pcs.crowdfunding.client.domain.Client;
import ru.pcs.crowdfunding.client.domain.ClientImage;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientForm {

    public static final String CLIENTS_IMAGE_PATH = "./client/src/main/resources/static/clients_images/";

//    @NotBlank
    private String firstName;

//    @NotBlank
    private String lastName;

//    @NotBlank
    private String country;

//    @NotBlank
    private String city;

//    @NotBlank
    private String email;

//    @NotBlank
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
