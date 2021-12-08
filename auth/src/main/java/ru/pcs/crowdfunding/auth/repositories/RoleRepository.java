package ru.pcs.crowdfunding.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pcs.crowdfunding.auth.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "select * from role where name = ?1", nativeQuery=true)
    Role getRoleByName(String name);
}
