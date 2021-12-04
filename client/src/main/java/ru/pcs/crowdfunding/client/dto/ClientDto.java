package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pcs.crowdfunding.client.domain.Client;
import ru.pcs.crowdfunding.client.domain.Project;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {

    private Long id;

    private String clientName;
    private String firstName;
    private String lastName;

    private String about;

    private String country;
    private String city;

    private String avatarImagePath;

    private BigDecimal sumAccount;
    private List<Project> projects;

    private String email;
    private String secondEmail;

    public static ClientDto from(Client client) {

        return ClientDto.builder()
                .id(client.getId())
                .sumAccount(transactionServiceRestTemplateClient.getBalance(client.getAccountId()))
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .country(client.getCountry())
                .city(client.getCity())
                .avatarImagePath(client.getAvatarImagePath())
                .projects(client.getProjects())
                .build();
    }

}
