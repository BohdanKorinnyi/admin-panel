package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.DashboardDao;
import com.omnia.admin.model.BuyerProfit;
import com.omnia.admin.service.QueryHelper;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@AllArgsConstructor
public class DashboardDaoImpl implements DashboardDao {
    private static final String SELECT_CHARTS = QueryHelper.loadQueryFromFile("buyer_dashboard_charts.sql");
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<BuyerProfit> findChartData(Integer buyerId, String from, String to, String filterName) {
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("buyerId", buyerId);
        if ("allTime".equals(filterName)) {
            source.addValue("from", "2016-01-01");
            source.addValue("to", "2020-01-01");
            return namedParameterJdbcTemplate.query(SELECT_CHARTS, source, BeanPropertyRowMapper.newInstance(BuyerProfit.class));
        } else if ("thisMonth".equals(filterName) || "lastMonth".equals(filterName)) {
            source.addValue("from", from);
            source.addValue("to", to);
            return namedParameterJdbcTemplate.query(SELECT_CHARTS.replaceAll("month\\(", "week\\("),
                    source, BeanPropertyRowMapper.newInstance(BuyerProfit.class));
        } else if ("lastWeek".equals(filterName)) {
            source.addValue("from", from);
            source.addValue("to", to);
            return namedParameterJdbcTemplate.query(SELECT_CHARTS.replaceAll("month\\(", "day\\("),
                    source, BeanPropertyRowMapper.newInstance(BuyerProfit.class));
        }
        return Collections.emptyList();
    }
}
