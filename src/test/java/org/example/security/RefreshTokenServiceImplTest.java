    package org.example.security;

    import org.example.dao.RefreshTokenDao;
    import org.example.dao.UserDao;
    import org.example.exception.EntityNotFoundException;
    import org.example.model.domain.RefreshToken;
    import org.example.model.domain.User;
    import org.example.security.service.RefreshTokenServiceImpl;
    import org.example.utils.TestObjectUtils;
    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.junit.jupiter.MockitoExtension;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;

    import java.util.Optional;

    import static org.junit.jupiter.api.Assertions.assertEquals;
    import static org.junit.jupiter.api.Assertions.assertNotNull;
    import static org.junit.jupiter.api.Assertions.assertThrows;
    import static org.mockito.ArgumentMatchers.any;
    import static org.mockito.ArgumentMatchers.anyString;
    import static org.mockito.Mockito.times;
    import static org.mockito.Mockito.verify;
    import static org.mockito.Mockito.when;

    @ExtendWith(MockitoExtension.class)
    public class RefreshTokenServiceImplTest {

        @Mock
        private RefreshTokenDao refreshTokenDao;
        @Mock
        private SecurityProperties securityProperties;
        @Mock
        private UserDao userDao;
        @Mock
        private JwtTokenProvider jwtTokenProvider;
        @InjectMocks
        private RefreshTokenServiceImpl refreshTokenService;

        @Test
        void shouldGenerateTokenWhenUserExistsTest() {
            // given
            RefreshToken expectedRefreshToken = TestObjectUtils.createTestRefreshToken();
            User expectedUser = TestObjectUtils.createTestUser();

            // when
            when(securityProperties.getRefreshExpiration()).thenReturn(1000L);
            when(securityProperties.getMaxRefreshTokensCount()).thenReturn(3);
            when(userDao.findByUsername(anyString())).thenReturn(Optional.of(expectedUser));
            when(refreshTokenDao.save(any(RefreshToken.class))).thenReturn(expectedRefreshToken);
            String actualRefreshToken = refreshTokenService.generateRefreshToken(expectedUser);

            // then
            assertNotNull(actualRefreshToken, "Refresh token should not be null");
            assertEquals(expectedRefreshToken.getToken(), actualRefreshToken, "Refresh token should match the expected one");
            verify(refreshTokenDao, times(1)).save(any(RefreshToken.class));
        }

        @Test
        void shouldThrowWhenUserNotFoundTest() {
            // given
            User expectedUser = TestObjectUtils.createTestUser();

            // when
            when(userDao.findByUsername(anyString())).thenReturn(Optional.empty());

            // then
            assertThrows(UsernameNotFoundException.class,
                    () -> refreshTokenService.generateRefreshToken(expectedUser),
                    "Should throw UsernameNotFoundException when user is not found");
        }

        @Test
        void shouldGenerateRefreshTokenDeleteOldTokensWhenMaxLimitExceededTest() {
            // given
            RefreshToken expectedRefreshToken = TestObjectUtils.createTestRefreshToken();
            User expectedUser = TestObjectUtils.createTestUser();

            // when
            when(userDao.findByUsername(anyString())).thenReturn(Optional.of(expectedUser));
            when(refreshTokenDao.countByUser(any(User.class))).thenReturn(5L);
            when(refreshTokenDao.save(any(RefreshToken.class))).thenReturn(expectedRefreshToken);
            String actualRefreshToken = refreshTokenService.generateRefreshToken(expectedUser);

            // then
            verify(refreshTokenDao, times(1)).deleteAllByUser(expectedUser);
            assertNotNull(actualRefreshToken, "Refresh token should not be null");
        }

        @Test
        void ShouldFindAndReturnRefreshTokenWhenTokenFoundTest() {
            // given
            RefreshToken expectedRefreshToken = TestObjectUtils.createTestRefreshToken();

            // when
            when(refreshTokenDao.findByToken(anyString())).thenReturn(Optional.of(expectedRefreshToken));
            RefreshToken actualToken = refreshTokenService.findByToken(expectedRefreshToken.getToken());

            // then
            assertNotNull(actualToken, "The refresh token shouldn't be null");
            assertEquals(expectedRefreshToken, actualToken, "The returned value should match the expected one");
        }

        @Test
        void ShouldThrowEntityNotFoundExceptionWhenTokenNotFoundTest() {
            // given
            RefreshToken expectedRefreshToken = TestObjectUtils.createTestRefreshToken();

            // when
            when(refreshTokenDao.findByToken(anyString())).thenReturn(Optional.empty());

            // then
            assertThrows(EntityNotFoundException.class, () -> refreshTokenService.findByToken(expectedRefreshToken.getToken()),
                    "Expected EntityNotFoundException to be thrown");
        }
    }
