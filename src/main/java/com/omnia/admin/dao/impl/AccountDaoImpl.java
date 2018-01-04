package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.AccountDao;
import com.omnia.admin.model.FinanceAccount;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class AccountDaoImpl implements AccountDao {
    private static final String SELECT_ACCOUNT_TYPES = "SELECT type FROM accounts GROUP BY type;";
    private static final String SELECT_FINANCE_ACCOUNTS = "SELECT fin_account.*, currency.code FROM fin_account " +
            "INNER JOIN currency ON fin_account.currency_id = currency.id;";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<String> getAccountTypes() {
        return jdbcTemplate.queryForList(SELECT_ACCOUNT_TYPES, String.class);
    }

    @Override
    public List<FinanceAccount> getFinanceAccounts() {
        return jdbcTemplate.query(SELECT_FINANCE_ACCOUNTS, BeanPropertyRowMapper.newInstance(FinanceAccount.class));
    }
}
