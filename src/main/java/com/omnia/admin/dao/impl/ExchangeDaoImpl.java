package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.ExchangeDao;
import com.omnia.admin.model.ExchangeRate;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class ExchangeDaoImpl implements ExchangeDao {
    private static final String SELECT_EXCHANGE_RATE = "SELECT " +
            "  exchange.rate, " +
            "  exchange.count " +
            "FROM currency " +
            "  INNER JOIN exchange ON currency.id = exchange.currency_id " +
            "WHERE code = ? " +
            "ORDER BY time DESC LIMIT 1;";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public ExchangeRate findExchangeRateByCurrencyCode(String code) {
        return jdbcTemplate.queryForObject(SELECT_EXCHANGE_RATE, BeanPropertyRowMapper.newInstance(ExchangeRate.class), code);
    }
}
