package org.example.service.web;

import org.example.web.dto.user.request.UserRoleRequest;
import org.example.web.dto.user.response.UserResponse;

public interface WebUserService {

    void exportUsers();

    UserResponse findUserById(Long id);

    void updateUserRole(Long id, UserRoleRequest request);

    void deactivateUser(Long id);
}
