package org.example.security;

import org.example.dao.UserDao;
import org.example.model.domain.User;
import org.example.security.service.UserDetailsServiceImpl;
import org.example.utils.TestObjectUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void ShouldLoadUserByUsernameAndReturnWhenUserExistsTest() {
        // given
        User expectedUser = TestObjectUtils.createTestUser();

        // when
        when(userDao.findByUsername(anyString())).thenReturn(Optional.of(expectedUser));
        UserDetails actualUser = userDetailsService.loadUserByUsername(expectedUser.getUsername());

        // then
        assertNotNull(actualUser, "UserDetails should not be null");
        assertEquals(expectedUser, actualUser, "Returned user should match expected one");
    }

    @Test
    void ShouldThrowExceptionWhenUserNotExistsTest() {
        // give
        // when
        when(userDao.findByUsername(anyString())).thenReturn(Optional.empty());

        // then
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("user"),
                "Should throw UsernameNotFoundException when user is not found");
    }
}
