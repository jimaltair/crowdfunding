package ru.pcs.crowdfunding.client.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pcs.crowdfunding.client.domain.Image;

/**
 * 07.12.2021
 * client
 *
 * @author Mitskevich Igor
 * @version v1.0
 */
@Repository
public interface ImagesRepository extends JpaRepository<Image, Long> {
}
