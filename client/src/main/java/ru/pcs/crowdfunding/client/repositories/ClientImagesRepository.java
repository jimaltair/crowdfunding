package ru.pcs.crowdfunding.client.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pcs.crowdfunding.client.domain.ClientImage;

public interface ClientImagesRepository extends JpaRepository<ClientImage, Long> {
}
