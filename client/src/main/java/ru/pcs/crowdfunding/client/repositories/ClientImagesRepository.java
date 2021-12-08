package ru.pcs.crowdfunding.client.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pcs.crowdfunding.client.domain.ClientImage;

public interface ClientImagesRepository extends JpaRepository<ClientImage, Long> {

    @Query(value = "select * from client_image where client_id = ?1", nativeQuery = true)
    byte[] getBytesImage(Long clientId);
}
