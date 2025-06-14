package org.example.service.domain;

import org.example.dao.UserDao;
import org.example.exception.EntityNotFoundException;
import org.example.model.domain.User;
import org.example.utils.TestObjectUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldGetByIdAndReturnUserWhenUserExistsTest() {
        // given
        User expectedUser = TestObjectUtils.createTestUser();

        // when
        when(userDao.findById(1L)).thenReturn(expectedUser);
        User actualUser = userService.getById(1L);

        // then
        assertNotNull(actualUser, "User should not be null");
        assertEquals(expectedUser, actualUser, "Return value should be equal expected one");
    }

    @Test
    void shouldNotGetByIdThrowExceptionWhenUserDoesNotExistTest() {
        // given

        // when
        when(userDao.findById(anyLong())).thenReturn(null);

        // then
        assertThrows(EntityNotFoundException.class,
                () -> userService.getById(999L),
                "Should throw EntityNotFoundException when User not found");
        verify(userDao, times(1)).findById(999L);
    }

    @Test
    void shouldCreateUserAndEncodePasswordTest() {
        // given
        User expectedUser = TestObjectUtils.createTestUser();

        // when
        when(userDao.save(any(User.class))).thenReturn(expectedUser);
        User actualUser = userService.createUser(expectedUser.getUsername(), expectedUser.getPassword());

        // then
        assertNotNull(actualUser, "Saved user shouldn't be null");
        assertEquals(expectedUser, actualUser, "Returned user should match the expected one");
        verify(userDao, times(1)).save(any(User.class));
    }

    @Test
    void shouldUpdateUserTest() {
        // given
        User expectedUser = TestObjectUtils.createTestUser();

        // when
        userService.updateUser(expectedUser);

        // then
        verify(userDao, times(1)).update(expectedUser);
    }

    @Test
    void shouldGetUsersAndReturnUserListTest() {
        // given
        User expectedUser = TestObjectUtils.createTestUser();

        // when
        when(userDao.findAll()).thenReturn(List.of(expectedUser));
        List<User> actualUsers = userService.getUsers();

        // then
        assertNotNull(actualUsers, "User list shouldn't be null");
        assertEquals(expectedUser, actualUsers.get(0), "Expected 1 user in list");
        verify(userDao, times(1)).findAll();
    }

    @Test
    void shouldGetUsersAndReturnEmptyListWhenNoUsersExistTest() {
        // given

        // when
        when(userDao.findAll()).thenReturn(Collections.emptyList());
        List<User> actualUsers = userService.getUsers();

        // then
        assertNotNull(actualUsers, "User list shouldn't be null");
        assertTrue(actualUsers.isEmpty(), "User list should be empty");
        verify(userDao, times(1)).findAll();
    }

    private static Stream<Arguments> paginationTestCases() {
        return Stream.of(
                Arguments.of(0, 5),
                Arguments.of(1, 5),
                Arguments.of(0, 10),
                Arguments.of(100, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("paginationTestCases")
    void shouldGetUsersWithPaginationAndReturnPaginatedUserListTest(int page, int size) {
        // given
        User expectedUser = TestObjectUtils.createTestUser();

        // when
        when(userDao.findAll(page, size)).thenReturn(List.of(expectedUser));
        List<User> actualUsers = userService.getUsers(page, size);

        // then
        assertNotNull(actualUsers, "User list shouldn't be null");
        assertEquals(expectedUser, actualUsers.get(0), "Expected 1 user in list");
        verify(userDao, times(1)).findAll(page, size);
    }

    @ParameterizedTest
    @MethodSource("paginationTestCases")
    void shouldGetUsersWithPaginationAndReturnEmptyListWhenNoUsersExistTest(int page, int size) {
        // given

        // when
        when(userDao.findAll(page, size)).thenReturn(Collections.emptyList());
        List<User> actualUsers = userService.getUsers(page, size);

        // then
        assertNotNull(actualUsers, "User list shouldn't be null");
        assertTrue(actualUsers.isEmpty(), "User list should be empty");
        verify(userDao, times(1)).findAll(page, size);
    }
}
