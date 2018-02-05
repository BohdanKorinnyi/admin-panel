package com.omnia.admin.dao;

import com.omnia.admin.model.User;

public interface UserDao {
    User getUserByName(String username);

    void changePassword(String username, String password);
}
