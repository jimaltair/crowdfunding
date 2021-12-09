package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pcs.crowdfunding.client.domain.Client;
import ru.pcs.crowdfunding.client.domain.ClientImage;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientForm {

    public static final String CLIENTS_IMAGE_PATH = "C:\\Users\\4801000\\IdeaProjects\\crowdfunding11111\\client\\src\\main\\resources\\static\\img\\1.jpg";

    private String firstName;

    private String lastName;

    private String country;

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
