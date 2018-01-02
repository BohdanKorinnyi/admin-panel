package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.AdminDashboardDao;
import com.omnia.admin.model.BuyerProfit;
import com.omnia.admin.service.QueryHelper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

import static com.omnia.admin.grid.filter.FilterConstant.EMPTY;

@Log4j
@Repository
@AllArgsConstructor
public class AdminDashboardDaoImpl implements AdminDashboardDao {
    private static final String DATE_CUSTOM_FORMAT = " AND date BETWEEN '%s' AND '%s'";
    private static final String DATE_TODAY_FORMAT = " AND date = DATE(now())";
    private static final String DATE_YESTERDAY_FORMAT = " AND date = (DATE(now()) - INTERVAL 1 DAY)";
    private static final String SELECT_PROFIT = QueryHelper.loadQueryFromFile("admin_dashboard_profit.sql");
    private static final String SELECT_TOTAL = QueryHelper.loadQueryFromFile("total_admin_dashboard_profit.sql");
    private static final String SELECT_CHARTS = QueryHelper.loadQueryFromFile("admin_dashboard_charts.sql");

    //TODO: remove one from templates
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<BuyerProfit> findAllBuyersProfit(String from, String to) {
        String whereClause = EMPTY;
        if (!StringUtils.isEmpty(from) && !StringUtils.isEmpty(to)) {
            whereClause = String.format(DATE_CUSTOM_FORMAT, from, to);
        }
        String sql = String.format(SELECT_PROFIT, whereClause, whereClause, whereClause, whereClause);
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(BuyerProfit.class));
    }

    @Override
    public List<BuyerProfit> findChartData(String from, String to, String filter) {
        if ("allTime".equals(filter)) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            // TODO: fix using MySQL: date(now() + INTERVAL 1 YEAR)
            source.addValue("from", "2018-01-01");
            source.addValue("to", "2018-01-31");
            return namedParameterJdbcTemplate.query(SELECT_CHARTS, source, BeanPropertyRowMapper.newInstance(BuyerProfit.class));
        } else if ("thisMonth".equals(filter) || "lastMonth".equals(filter)) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("from", from);
            source.addValue("to", to);
            return namedParameterJdbcTemplate.query(SELECT_CHARTS.replaceAll("month\\(", "week\\("),
                    source, BeanPropertyRowMapper.newInstance(BuyerProfit.class));
        } else if ("lastWeek".equals(filter)) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("from", from);
            source.addValue("to", to);
            return namedParameterJdbcTemplate.query(SELECT_CHARTS.replaceAll("month\\(", "day\\("),
                    source, BeanPropertyRowMapper.newInstance(BuyerProfit.class));
        } else if ("day".equals(filter)) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("from", from);
            source.addValue("to", to);
            return namedParameterJdbcTemplate.query(SELECT_CHARTS.replaceAll("month\\(", "date\\("),
                    source, BeanPropertyRowMapper.newInstance(BuyerProfit.class));
        }
        return Collections.emptyList();
    }

    @Override
    public BuyerProfit findRecentBuyersProfit(boolean today) {
        String sql;
        //TODO: don't use String.format for SQL building
        if (today) {
            sql = String.format(SELECT_TOTAL, DATE_TODAY_FORMAT, DATE_TODAY_FORMAT, DATE_TODAY_FORMAT, DATE_TODAY_FORMAT);
        } else {
            sql = String.format(SELECT_TOTAL, DATE_YESTERDAY_FORMAT, DATE_YESTERDAY_FORMAT, DATE_YESTERDAY_FORMAT, DATE_YESTERDAY_FORMAT);
        }
        return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(BuyerProfit.class));
    }
}
