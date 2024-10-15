package com.trungnguyen.userservice.service;

import com.trungnguyen.userservice.data.Role;
import com.trungnguyen.userservice.data.User;
import com.trungnguyen.userservice.model.UserDTO;

import java.util.List;

public interface IUserService {
    List<User> getAllUser();
    List<Role> getAllRole();
    User saveUser(User user);
    Role saveRole(Role role);
    void addRole(String username,String roleName);
    UserDTO login(String username, String password);
    UserDTO validateToken(String token);
}
