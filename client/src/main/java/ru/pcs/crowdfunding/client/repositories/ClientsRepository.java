package ru.pcs.crowdfunding.client.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pcs.crowdfunding.client.domain.Client;

public interface ClientsRepository extends JpaRepository<Client, Long> {
}
