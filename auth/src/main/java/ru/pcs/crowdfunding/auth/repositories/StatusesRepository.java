package ru.pcs.crowdfunding.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pcs.crowdfunding.auth.domain.Status;

public interface StatusesRepository extends JpaRepository<Status, Long> {
    Status getByName(Status.StatusEnum name);
}
