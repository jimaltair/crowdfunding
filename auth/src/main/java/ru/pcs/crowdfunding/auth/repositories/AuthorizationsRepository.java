package ru.pcs.crowdfunding.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pcs.crowdfunding.auth.domain.Authorization;

public interface AuthorizationsRepository extends JpaRepository<Authorization, Long> {
}
