package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.AdminDashboardDao;
import com.omnia.admin.model.BuyerProfit;
import com.omnia.admin.service.QueryHelper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

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
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<BuyerProfit> findAllBuyersProfit(String from, String to) {
        String whereClause = EMPTY;
        if (!StringUtils.isEmpty(from) && !StringUtils.isEmpty(to)) {
            whereClause = String.format(DATE_CUSTOM_FORMAT, from, to);
        }
        String sql = String.format(SELECT_PROFIT, whereClause, whereClause, whereClause, whereClause);
        log.info("sql by period=" + sql);
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(BuyerProfit.class)
        );
    }

    @Override
    public BuyerProfit findRecentBuyersProfit(boolean today) {
        if (today) {
            String sql = String.format(SELECT_TOTAL, DATE_TODAY_FORMAT, DATE_TODAY_FORMAT, DATE_TODAY_FORMAT, DATE_TODAY_FORMAT);
            log.info("sql today=" + sql);
            return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(BuyerProfit.class)
            );
        }
        String sql = String.format(SELECT_TOTAL, DATE_YESTERDAY_FORMAT, DATE_YESTERDAY_FORMAT, DATE_YESTERDAY_FORMAT, DATE_YESTERDAY_FORMAT);
        log.info("sql yesterday=" + sql);
        return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(BuyerProfit.class)
        );
    }
}
