package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.BuyerDao;
import com.omnia.admin.model.Buyer;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class BuyerDaoImpl implements BuyerDao {

    private static final String SELECT_ALL_BUYERS_NAME = "SELECT name FROM buyers ORDER BY name ASC;";
    private static final String SELECT_ALL_BUYERS = "SELECT * FROM buyers ORDER BY name ASC;";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<String> getBuyersName() {
        return jdbcTemplate.queryForList(SELECT_ALL_BUYERS_NAME, String.class);
    }

    @Override
    public List<Buyer> getBuyers() {
        return jdbcTemplate.query(SELECT_ALL_BUYERS, new BeanPropertyRowMapper<>(Buyer.class));
    }
}
