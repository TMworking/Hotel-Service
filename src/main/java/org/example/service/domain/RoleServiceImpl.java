package org.example.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.RoleDao;
import org.example.exception.EntityNotFoundException;
import org.example.model.domain.Role;
import org.example.model.enums.UserRole;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    @Override
    public Role findByName(UserRole userRole) {
        log.debug("Attempting to find role by name: {}", userRole);
        Role role = roleDao.findByName(userRole)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Role {0} not exist", userRole))
                );
        log.debug("Role found by name: {}", role);
        return role;
    }
}
