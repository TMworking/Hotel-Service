package org.example.service.domain;

import org.example.model.domain.Role;
import org.example.model.enums.UserRole;

public interface RoleService {

    Role findByName(UserRole role);
}
