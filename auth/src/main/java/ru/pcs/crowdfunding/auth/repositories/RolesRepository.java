package ru.pcs.crowdfunding.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pcs.crowdfunding.auth.domain.Role;

public interface RolesRepository extends JpaRepository<Role, Long> {
    Role getByName(Role.RoleEnum name);
}
