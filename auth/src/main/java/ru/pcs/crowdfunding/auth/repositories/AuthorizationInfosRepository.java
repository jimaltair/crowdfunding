package ru.pcs.crowdfunding.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pcs.crowdfunding.auth.domain.AuthorizationInfo;

public interface AuthorizationInfosRepository extends JpaRepository<AuthorizationInfo, Long> {
}
