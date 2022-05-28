package com.example.pressnews.service;

import com.example.pressnews.model.Role;
import com.example.pressnews.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    List<User>getUsers();
}
