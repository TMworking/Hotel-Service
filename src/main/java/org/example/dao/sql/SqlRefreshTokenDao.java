package org.example.dao.sql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.example.dao.RefreshTokenDao;
import org.example.model.domain.RefreshToken;
import org.example.model.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SqlRefreshTokenDao implements RefreshTokenDao {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        try {
            RefreshToken refreshToken = entityManager.createQuery(
                            "SELECT rt FROM RefreshToken rt WHERE rt.token = :token", RefreshToken.class)
                    .setParameter("token", token)
                    .getSingleResult();
            return Optional.of(refreshToken);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<RefreshToken> findByUser(User user) {
        try {
            RefreshToken refreshToken = entityManager.createQuery(
                            "SELECT rt FROM RefreshToken rt WHERE rt.user = :user", RefreshToken.class)
                    .setParameter("user", user)
                    .getSingleResult();
            return Optional.of(refreshToken);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        entityManager.persist(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken update(RefreshToken refreshToken) {
        return entityManager.merge(refreshToken);
    }

    @Override
    public void deleteAllByUser(User user) {
        entityManager.createQuery(
                        "DELETE FROM RefreshToken rt WHERE rt.user = :user")
                .setParameter("user", user)
                .executeUpdate();
    }

    @Override
    public Long countByUser(User user) {
        return (Long) entityManager.createQuery("SELECT COUNT(rt) FROM RefreshToken rt WHERE rt.user = :user")
                .setParameter("user", user)
                .getSingleResult();
    }
}
