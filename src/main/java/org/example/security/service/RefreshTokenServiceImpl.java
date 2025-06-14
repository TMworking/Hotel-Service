package org.example.security.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.RefreshTokenDao;
import org.example.dao.UserDao;
import org.example.exception.EntityNotFoundException;
import org.example.model.domain.RefreshToken;
import org.example.model.domain.User;
import org.example.security.JwtTokenProvider;
import org.example.security.SecurityProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenDao refreshTokenDao;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDao userDao;
    private final SecurityProperties securityProperties;

    @Transactional
    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        User user = userDao.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Long tokensCount = refreshTokenDao.countByUser(user);
        if (tokensCount >= securityProperties.getMaxRefreshTokensCount()) {
            refreshTokenDao.deleteAllByUser(user);
        }

        RefreshToken refreshToken = saveToken(user);
        return refreshToken.getToken();
    }

    @Override
    public String generateAccessToken(UserDetails userDetails) {
        return jwtTokenProvider.generateAccessToken(userDetails);
    }

    private RefreshToken saveToken(User user) {
        RefreshToken refreshToken;
        refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(securityProperties.getRefreshExpiration()));
        return refreshTokenDao.save(refreshToken);
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenDao.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Invalid refresh token"));
    }
}
