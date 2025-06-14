package org.example.service.web;

import org.example.web.dto.auth.AuthResponse;
import org.example.web.dto.auth.RefreshTokenRequest;
import org.example.web.dto.user.request.UserLoginRequest;
import org.example.web.dto.user.request.UserRegisterRequest;

public interface WebAuthService {
    void register(UserRegisterRequest request);

    AuthResponse login(UserLoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);
}
