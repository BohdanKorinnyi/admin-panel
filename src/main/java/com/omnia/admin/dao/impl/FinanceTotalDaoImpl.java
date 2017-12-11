package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.FinanceTotalDao;
import com.omnia.admin.model.FinanceTotal;
import com.omnia.admin.service.QueryHelper;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class FinanceTotalDaoImpl implements FinanceTotalDao {
    private static final String SELECT_FINANCE_TOTAL_BY_TIME_RANGE = QueryHelper.loadQueryFromFile("total_finance.sql");
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<FinanceTotal> findFinanceTotalByTimeRange(String from, String to) {
        Map<String, String> values = new HashMap<>();
        values.put("from", from);
        values.put("to", to);
        return namedParameterJdbcTemplate.query(
                SELECT_FINANCE_TOTAL_BY_TIME_RANGE, values, BeanPropertyRowMapper.newInstance(FinanceTotal.class)
        );
    }
}
