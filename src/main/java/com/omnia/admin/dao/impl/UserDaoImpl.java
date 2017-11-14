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

    private static final String SELECT_USER_BY_USERNAME = "SELECT user_id,username,password,name,role_id,group_id,buyer_id FROM users WHERE username = ?;";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User getUserByName(String username) {
        long start = System.currentTimeMillis();
        User user = jdbcTemplate.queryForObject(SELECT_USER_BY_USERNAME, new BeanPropertyRowMapper<>(User.class), username);
        log.info("Select user by username executed in " + (System.currentTimeMillis() - start) + " ms, sql=" + SELECT_USER_BY_USERNAME);
        return user;
    }
}
