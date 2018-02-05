package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.UserDao;
import com.omnia.admin.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Log4j
@Repository
@AllArgsConstructor
public class UserDaoImpl implements UserDao {
    private static final String SELECT_USER_BY_USERNAME = "SELECT user_id,username,password,roles.name AS 'role',group_id, " +
            "buyer_id FROM users INNER JOIN roles ON users.role_id = roles.id WHERE username = ?;";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User getUserByName(String username) {
        return jdbcTemplate.queryForObject(SELECT_USER_BY_USERNAME, new BeanPropertyRowMapper<>(User.class), username);
    }

    @Override
    public void changePassword(String username, String password) {
        jdbcTemplate.update("UPDATE users SET password = ? WHERE username= ?", password, username);
    }
}
