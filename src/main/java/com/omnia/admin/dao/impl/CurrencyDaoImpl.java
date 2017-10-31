package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.CurrencyDao;
import com.omnia.admin.model.Currency;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class CurrencyDaoImpl implements CurrencyDao {

    private static final String SELECT_CURRENCY = "SELECT id,code,descriptions FROM currency;";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Currency> getAllCurrency() {
        return jdbcTemplate.query(SELECT_CURRENCY, new BeanPropertyRowMapper<>(Currency.class));
    }
}
