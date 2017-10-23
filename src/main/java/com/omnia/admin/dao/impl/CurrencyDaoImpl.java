package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.CurrencyDao;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class CurrencyDaoImpl implements CurrencyDao {

    private static final String SELECT_CURRENCY_ID_BY_POSTBACK_ID = "SELECT currency.id " +
            "FROM currency" +
            "  LEFT JOIN postback ON postback.currency = currency.code " +
            "WHERE postback.id = ?;";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Integer getCurrencyIdByPostback(String postbackCurrency) {
        return jdbcTemplate.queryForObject(SELECT_CURRENCY_ID_BY_POSTBACK_ID, Integer.class);
    }
}
