package org.example.dao;

import org.example.model.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    User findById(Long id);

    List<User> findAll();

    List<User> findAll(int pageNumber, int pageSize);

    Optional<User> findByUsername(String username);

    User save(User entity);

    void update(User user);

    Long countAll();
}
