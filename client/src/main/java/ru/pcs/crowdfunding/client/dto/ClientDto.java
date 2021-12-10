package ru.pcs.crowdfunding.client.dto;

import lombok.*;
import ru.pcs.crowdfunding.client.domain.Client;
import ru.pcs.crowdfunding.client.domain.ClientImage;
import ru.pcs.crowdfunding.client.domain.Project;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"image"})
@EqualsAndHashCode(exclude = {"image"})
public class ClientDto {

    private Long id;

    private String clientName;
    private String firstName;
    private String lastName;

    private String country;
    private String city;

    private ClientImage image;

    private BigDecimal sumAccount;
    private List<Project> projects;

    private String email;

    public static ClientDto from(Client client) {

        return ClientDto.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .country(client.getCountry())
                .city(client.getCity())
                .image(client.getImage())
                .projects(client.getProjects())
                .build();
    }

}
