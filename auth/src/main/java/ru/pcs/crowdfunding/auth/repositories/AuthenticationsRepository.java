package ru.pcs.crowdfunding.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pcs.crowdfunding.auth.domain.Authentication;

public interface AuthenticationsRepository extends JpaRepository<Authentication, Long> {
}
