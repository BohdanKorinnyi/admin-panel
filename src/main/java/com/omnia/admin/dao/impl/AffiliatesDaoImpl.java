package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.AffiliatesDao;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class AffiliatesDaoImpl implements AffiliatesDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Long> getAffiliatesIdsByBuyerId(long buyerId) {
        return jdbcTemplate.queryForList("SELECT afid FROM affiliates WHERE buyer_id = ?;", Long.class, buyerId);
    }

    @Override
    public List<Long> getAffiliatesIdsByBuyerId() {
        return jdbcTemplate.queryForList("SELECT afid FROM affiliates;", Long.class);
    }

    @Override
    public void generate(long afid, long buyerId) {
        jdbcTemplate.update("INSERT INTO affiliates (afid, buyer_id) VALUES (?, ?);", afid, buyerId);
    }
}
