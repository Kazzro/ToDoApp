package de.inmediasp.TodoApp.dao;

import de.inmediasp.TodoApp.entity.Role;
import de.inmediasp.TodoApp.entity.RoleEnums;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnums name);
}
