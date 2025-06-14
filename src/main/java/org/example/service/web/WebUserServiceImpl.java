package org.example.service.web;

import lombok.RequiredArgsConstructor;
import org.example.exception.CsvExportException;
import org.example.mappers.UserMapper;
import org.example.model.domain.User;
import org.example.service.domain.ExportService;
import org.example.service.domain.RoleService;
import org.example.service.domain.UserService;
import org.example.web.dto.user.request.UserRoleRequest;
import org.example.web.dto.user.response.UserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WebUserServiceImpl implements WebUserService {

    private final UserService userService;
    private final ExportService exportService;
    private final UserMapper userMapper;
    private final RoleService roleService;


    @Value("${userTomcatExportPath}")
    private String usersExportPath;

    @Override
    public void exportUsers() {
        try {
            List<User> bookings = userService.getUsers();
            exportService.exportToCsv(bookings, usersExportPath);
        } catch (IOException e) {
            throw new CsvExportException("Failed to export users: " + e.getMessage());
        }
    }

    @Override
    public UserResponse findUserById(Long id) {
        User user = userService.getById(id);
        return userMapper.toResponse(user);
    }

    @Override
    public void updateUserRole(Long id, UserRoleRequest request) {
        User user = userService.getById(id);
        user.setRole(roleService.findByName(request.getRole()));
        userService.updateUser(user);
    }

    @Override
    public void deactivateUser(Long id) {
        User user = userService.getById(id);
        user.setIsEnabled(false);
        userService.updateUser(user);
    }
}
