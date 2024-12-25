package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {

    UserDetails loadUserByUsername(String name);

    void addUser(User user);

    void saveUser(User user, List<Role> roles);

    List<User> getAllUsers();

    User getUser(Long id);

    void updateUser(User user, List<Role> roles);

    void deleteUser(long id);

    User findByUserName(String name);

    Role findByName(String role);
}