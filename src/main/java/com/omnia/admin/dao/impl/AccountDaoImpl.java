package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.AccountDao;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class AccountDaoImpl implements AccountDao {

    private static final String SELECT_ACCOUNT_TYPES = "SELECT type FROM accounts GROUP BY type;";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<String> getAccountTypes() {
        return jdbcTemplate.queryForList(SELECT_ACCOUNT_TYPES, String.class);
    }
}
