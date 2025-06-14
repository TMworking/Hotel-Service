package org.example.dao.sql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.example.dao.RoleDao;
import org.example.model.domain.Role;
import org.example.model.enums.UserRole;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SqlRoleDao implements RoleDao {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Role> findByName(UserRole userRole) {
        try {
            return Optional.of(entityManager.createQuery(
                            "FROM Role ro WHERE ro.roleName = :role", Role.class)
                    .setParameter("role", userRole)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
