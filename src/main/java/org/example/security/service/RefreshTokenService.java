package org.example.security.service;

import org.example.model.domain.RefreshToken;
import org.springframework.security.core.userdetails.UserDetails;

public interface RefreshTokenService {

    String generateRefreshToken(UserDetails userDetails);

    String generateAccessToken(UserDetails userDetails);

    RefreshToken findByToken(String token);
}
