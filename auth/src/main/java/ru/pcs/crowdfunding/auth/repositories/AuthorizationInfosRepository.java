package ru.pcs.crowdfunding.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pcs.crowdfunding.auth.domain.AuthorizationInfo;

import java.util.Optional;

public interface AuthorizationInfosRepository extends JpaRepository<AuthorizationInfo, Long> {
    Optional<AuthorizationInfo> findByUserId(Long userId);
}
