package org.example.service.web;

import lombok.RequiredArgsConstructor;
import org.example.model.domain.RefreshToken;
import org.example.security.service.RefreshTokenService;
import org.example.service.domain.UserService;
import org.example.web.dto.auth.AuthResponse;
import org.example.web.dto.auth.RefreshTokenRequest;
import org.example.web.dto.user.request.UserLoginRequest;
import org.example.web.dto.user.request.UserRegisterRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebAuthServiceImpl implements WebAuthService {

    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @Override
    public AuthResponse login(UserLoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        String refreshToken = refreshTokenService.generateRefreshToken(userDetails);
        String accessToken = refreshTokenService.generateAccessToken(userDetails);
        return new AuthResponse(refreshToken, accessToken);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken storedToken = refreshTokenService.findByToken(request.getRefreshToken());

        UserDetails userDetails = userDetailsService.loadUserByUsername(
                storedToken.getUser().getUsername());

        String refreshToken = refreshTokenService.generateRefreshToken(userDetails);
        String accessToken = refreshTokenService.generateAccessToken(userDetails);
        return new AuthResponse(refreshToken, accessToken);
    }

    @Override
    public void register(UserRegisterRequest request) {
        userService.createUser(request.getUsername(), request.getPassword());
    }
}
