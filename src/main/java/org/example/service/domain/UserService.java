package org.example.service.domain;

import org.example.model.domain.User;

import java.util.List;

public interface UserService {

    User getById(Long id);

    User createUser(String username, String password);

    void updateUser(User user);

    List<User> getUsers();

    List<User> getUsers(int pageNumber, int pageSize);
}
