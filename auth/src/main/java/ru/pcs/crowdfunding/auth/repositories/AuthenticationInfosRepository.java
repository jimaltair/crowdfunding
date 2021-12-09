package ru.pcs.crowdfunding.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pcs.crowdfunding.auth.domain.AuthenticationInfo;

import java.util.Optional;

public interface AuthenticationInfosRepository extends JpaRepository<AuthenticationInfo, Long> {

    Optional<AuthenticationInfo> findByEmail(String email);
}
