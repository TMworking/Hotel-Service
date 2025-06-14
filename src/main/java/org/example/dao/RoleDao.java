package org.example.dao;

import org.example.model.domain.Role;
import org.example.model.enums.UserRole;

import java.util.Optional;

public interface RoleDao {

    Optional<Role> findByName(UserRole role);
}
