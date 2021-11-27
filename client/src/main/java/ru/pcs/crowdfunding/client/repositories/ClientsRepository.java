package ru.pcs.crowdfunding.client.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pcs.crowdfunding.client.domain.Client;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientsRepository extends JpaRepository<Client, Long> {
    Optional<Client> findClientById(Long id);
    Optional<Client> findClientByAccountId(Long accountId);

    Page<Client> findAllByProjectsIsNotNull(Pageable pageable);

    Integer countAllByCountry(String country);
    Integer countAllByCity(String city);

    Page<Client> findClientsByFirstNameContainsOrLastNameContains(String firstName, String lastName, Pageable pageable);
    Page<Client> findClientsByCountryContains(String country, Pageable pageable);
    Page<Client> findClientsByCityContains(String city, Pageable pageable);
}
