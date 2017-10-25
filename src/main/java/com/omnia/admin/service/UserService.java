package com.omnia.admin.service;

import com.omnia.admin.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserByName(String username);
}
