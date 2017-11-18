package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.BuyerKpiDao;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class BuyerKpiDaoImpl implements BuyerKpiDao {
    private final JdbcTemplate jdbcTemplate;
}
