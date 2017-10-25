package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.UserDao;
import com.omnia.admin.model.User;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserDaoImpl implements UserDao {

    private static final String SELECT_USER_BY_USERNAME = "SELECT user_id,username,password,name,first_name,secod_name,role_id,group_id,buyer_id FROM users WHERE username = ?;";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User getUserByName(String username) {
        return jdbcTemplate.queryForObject(SELECT_USER_BY_USERNAME, new BeanPropertyRowMapper<>(User.class), username);
    }
}
