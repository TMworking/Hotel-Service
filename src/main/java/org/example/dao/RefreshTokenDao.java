package org.example.dao;

import org.example.model.domain.RefreshToken;
import org.example.model.domain.User;

import java.util.Optional;

public interface RefreshTokenDao {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(User user);

    RefreshToken save(RefreshToken refreshToken);

    RefreshToken update(RefreshToken refreshToken);

    void deleteAllByUser(User user);

    Long countByUser(User user);
}
