package org.example.service.web;

import org.example.exception.EntityNotFoundException;
import org.example.model.domain.RefreshToken;
import org.example.model.domain.Role;
import org.example.model.domain.User;
import org.example.model.domain.Visitor;
import org.example.security.service.RefreshTokenService;
import org.example.service.domain.UserService;
import org.example.utils.TestObjectUtils;
import org.example.web.dto.auth.AuthResponse;
import org.example.web.dto.auth.RefreshTokenRequest;
import org.example.web.dto.user.request.UserLoginRequest;
import org.example.web.dto.user.request.UserRegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WebAuthServiceImplTest {

    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private UserService userService;
    @InjectMocks
    private WebAuthServiceImpl webAuthService;

    @Test
    void shouldLoginAndReturnAuthResponseWhenValidCredentialsTest() {
        // given
        User expectedUser = TestObjectUtils.createTestUser();
        RefreshToken expectedRefreshToken = TestObjectUtils.createTestRefreshToken();
        String expectedAccessToken = TestObjectUtils.createAccessToken();
        UserLoginRequest loginRequest = TestObjectUtils.createUserLoginRequest();
        Authentication authentication = mock(Authentication.class);

        // when
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(expectedUser);
        when(refreshTokenService.generateRefreshToken(any(User.class))).thenReturn(expectedRefreshToken.getToken());
        when(refreshTokenService.generateAccessToken(any(User.class))).thenReturn(expectedAccessToken);
        AuthResponse response = webAuthService.login(loginRequest);

        // then
        assertNotNull(response);
        assertEquals(expectedRefreshToken.getToken(), response.getRefreshToken());
        assertEquals(expectedAccessToken, response.getAccessToken());
    }

    @Test
    void shouldNotLoginAndThrowBadCredentialsExceptionWhenAuthenticationFailsTest() {
        // given
        UserLoginRequest loginRequest = TestObjectUtils.createUserLoginRequest();

        // when
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // then
        assertThrows(BadCredentialsException.class, () -> webAuthService.login(loginRequest));
    }

    @Test
    void shouldRefreshTokenAndReturnNewAuthResponseWhenValidRefreshTokenTest() {
        // given
        User expectedUser = TestObjectUtils.createTestUser();
        RefreshToken expectedRefreshToken = TestObjectUtils.createTestRefreshToken();
        String expectedAccessToken = TestObjectUtils.createAccessToken();
        RefreshTokenRequest expectedRefreshTokenRequest = TestObjectUtils.createRefreshTokenRequest();

        // when
        when(refreshTokenService.findByToken(anyString())).thenReturn(expectedRefreshToken);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(expectedUser);
        when(refreshTokenService.generateRefreshToken(any(UserDetails.class))).thenReturn(expectedRefreshToken.getToken());
        when(refreshTokenService.generateAccessToken(any(UserDetails.class))).thenReturn(expectedAccessToken);
        AuthResponse actualTokens = webAuthService.refreshToken(expectedRefreshTokenRequest);

        // then
        assertNotNull(actualTokens);
        assertEquals(expectedRefreshToken.getToken(), actualTokens.getRefreshToken());
        assertEquals(expectedAccessToken, actualTokens.getAccessToken());
    }

    @Test
    void shouldNotRefreshTokenAndThrowEntityNotFoundExceptionWhenTokenNotFoundTest() {
        // given
        RefreshTokenRequest expectedRefreshTokenRequest = TestObjectUtils.createRefreshTokenRequest();

        // when
        when(refreshTokenService.findByToken(anyString())).thenThrow(
                new EntityNotFoundException("Invalid refresh token"));

        // then
        assertThrows(EntityNotFoundException.class, () -> webAuthService.refreshToken(expectedRefreshTokenRequest));
    }

    @Test
    void shouldRegisterAndCallCreateUserWhenValidRequestTest() {
        // given
        User expectedUser = TestObjectUtils.createTestUser();
        UserRegisterRequest expectedRequest = TestObjectUtils.createUserRegisterRequest();

        // when
        webAuthService.register(expectedRequest);

        // then
        verify(userService, times(1)).createUser(expectedUser.getUsername(), expectedUser.getPassword());
    }
}
