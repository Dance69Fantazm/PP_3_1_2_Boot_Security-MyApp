package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.dao.UserDaoImp;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService, UserDetailsService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final UserDaoImp userDaoImp;

    @Autowired
    public UserServiceImp(UserDao userDao, PasswordEncoder passwordEncoder, UserDaoImp userDaoImp) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.userDaoImp = userDaoImp;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = findByUserName(name);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден: " + name);
        }
        return user;
    }

    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.addUser(user);
    }

    @Override
    @Transactional
    public void saveUser(User user, List<Role> roles) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        userDao.saveUser(user, roles);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public User getUser(Long id) {
        return userDao.getUser(id);
    }

    @Override
    @Transactional
    public void updateUser(User updatedUser, List<Role> roles) {
        User UserInf = getUser(updatedUser.getId());
        UserInf.setName(updatedUser.getUsername());
        UserInf.setSurname(updatedUser.getSurname());
        UserInf.setAge(updatedUser.getAge());
        UserInf.setRoles(roles);
        UserInf.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        userDao.updateUser(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        userDao.deleteUser(id);
    }

    @Override
    public User findByUserName(String name) {
        return userDao.findByUserName(name);
    }

    @Override
    public Role findByName(String role) {
        return userDao.findByName(role);
    }
}
