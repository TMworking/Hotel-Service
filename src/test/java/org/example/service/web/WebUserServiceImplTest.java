package org.example.service.web;

import org.example.mappers.UserMapper;
import org.example.model.domain.Role;
import org.example.model.domain.User;
import org.example.model.enums.UserRole;
import org.example.service.domain.ExportService;
import org.example.service.domain.RoleService;
import org.example.service.domain.UserService;
import org.example.utils.TestObjectUtils;
import org.example.web.dto.user.request.UserRoleRequest;
import org.example.web.dto.user.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WebUserServiceImplTest {

    @Mock
    private UserService userService;
    @Mock
    private ExportService exportService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private RoleService roleService;
    @InjectMocks
    private WebUserServiceImpl webUserService;

    @Test
    void shouldFindUserByIdAndReturnMappedUserTest() {
        // given
        User expectedUser = TestObjectUtils.createTestUser();
        UserResponse expectedResponse = new UserResponse();

        // when
        when(userService.getById(anyLong())).thenReturn(expectedUser);
        when(userMapper.toResponse(expectedUser)).thenReturn(expectedResponse);
        UserResponse actualResponse = webUserService.findUserById(1L);

        // then
        assertEquals(expectedResponse, actualResponse);
        verify(userService).getById(1L);
        verify(userMapper).toResponse(expectedUser);
    }

    @Test
    void shouldUpdateUserRoleTest() {
        // given
        User expectedUser = TestObjectUtils.createTestUser();
        Role expectedRole = TestObjectUtils.createTestRole();
        UserRoleRequest expectedRequest = new UserRoleRequest(UserRole.USER);

        // when
        when(userService.getById(anyLong())).thenReturn(expectedUser);
        when(roleService.findByName(UserRole.USER)).thenReturn(expectedRole);
        webUserService.updateUserRole(1L, expectedRequest);

        // then
        assertNotNull(expectedUser.getRole());
        verify(userService).updateUser(expectedUser);
    }

    @Test
    void shouldDeactivateUserAndSetUserDisabledTest() {
        // given
        User expectedUser = TestObjectUtils.createTestUser();

        // when
        when(userService.getById(anyLong())).thenReturn(expectedUser);
        webUserService.deactivateUser(1L);

        // then
        assertFalse(expectedUser.getIsEnabled());
        verify(userService).updateUser(expectedUser);
    }
}
