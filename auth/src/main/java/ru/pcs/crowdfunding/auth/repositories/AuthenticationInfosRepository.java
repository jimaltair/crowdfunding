package ru.pcs.crowdfunding.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pcs.crowdfunding.auth.domain.AuthenticationInfo;

public interface AuthenticationInfosRepository extends JpaRepository<AuthenticationInfo, Long> {
    Boolean findByEmail(String email);
}
