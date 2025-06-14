package org.example.service.domain;

import org.example.dao.RoleDao;
import org.example.exception.EntityNotFoundException;
import org.example.model.domain.Role;
import org.example.model.enums.UserRole;
import org.example.utils.TestObjectUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    @Mock
    private RoleDao roleDao;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void shouldFindByNameAndReturnRoleWhenRoleExistsTest() {
        // given
        Role expectedRole = TestObjectUtils.createTestRole();

        // when
        when(roleDao.findByName(any(UserRole.class))).thenReturn(Optional.of(expectedRole));
        Role actualRole = roleService.findByName(expectedRole.getRoleName());

        // then
        assertNotNull(actualRole, "Role shouldn't be null");
        assertEquals(expectedRole, actualRole, "Role name should match");
        verify(roleDao, times(1)).findByName(expectedRole.getRoleName());
    }

    @Test
    void shouldNotFindByNameAndThrowEntityNotFoundExceptionWhenRoleDoesNotExistTest() {
        // given
        Role expectedRole = TestObjectUtils.createTestRole();

        // when
        when(roleDao.findByName(any(UserRole.class))).thenReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> roleService.findByName(expectedRole.getRoleName()));
        verify(roleDao, times(1)).findByName(expectedRole.getRoleName());
    }
}
