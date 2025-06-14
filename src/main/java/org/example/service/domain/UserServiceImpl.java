package org.example.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.UserDao;
import org.example.exception.EntityNotFoundException;
import org.example.model.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao sqlUserDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getById(Long id) throws EntityNotFoundException {
        log.debug("Attempting to find user by id: {}", id);
        User user = sqlUserDao.findById(id);
        if (user == null) {
            throw new EntityNotFoundException(
                    MessageFormat.format("User with {0} id not found", id));
        }
        log.debug("User found: {}", user);
        return user;
    }

    @Transactional
    @Override
    public User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        log.debug("Registration new user: username = {}, role = {}", user.getUsername(), user.getRole());
        User result = sqlUserDao.save(user);
        log.debug("User registered successfully");
        return result;
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        sqlUserDao.update(user);
    }

    @Override
    public List<User> getUsers() {
        log.debug("Fetching all users");
        List<User> users = sqlUserDao.findAll();
        log.debug("Fetched {} users", users.size());
        return users;
    }

    @Override
    public List<User> getUsers(int pageNumber, int pageSize) {
        log.debug("Fetching all users with pagination");
        List<User> users = sqlUserDao.findAll(pageNumber, pageSize);
        log.debug("Fetched {} users with pagination", users.size());
        return users;
    }
}
