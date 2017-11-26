package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.AdminDashboardDao;
import com.omnia.admin.model.BuyerProfit;
import com.omnia.admin.service.QueryHelper;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.omnia.admin.grid.filter.FilterConstant.EMPTY;

@Repository
@AllArgsConstructor
public class AdminDashboardDaoImpl implements AdminDashboardDao {
    private static final String DATE_CUSTOM_FORMAT = " AND date BETWEEN '%s' AND '%s'";
    private static final String DATE_TODAY_FORMAT = " AND date = now()";
    private static final String DATE_YESTERDAY_FORMAT = " AND date = (now() - INTERVAL 1 DAY)";
    private static final String SELECT_PROFIT = QueryHelper.loadQueryFromFile("admin_dashboard_profit.sql");
    private static final String SELECT_TOTAL = QueryHelper.loadQueryFromFile("total_admin_dashboard_profit.sql");
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<BuyerProfit> findAllBuyersProfit(String from, String to) {
        String whereClause = EMPTY;
        if (!StringUtils.isEmpty(from) && !StringUtils.isEmpty(to)) {
            whereClause = String.format(DATE_CUSTOM_FORMAT, from, to);
        }
        return jdbcTemplate.query(String.format(SELECT_PROFIT, whereClause, whereClause, whereClause, whereClause),
                BeanPropertyRowMapper.newInstance(BuyerProfit.class)
        );
    }

    @Override
    public BuyerProfit findRecentBuyersProfit(boolean today) {
        if (today) {
            return jdbcTemplate.queryForObject(String.format(SELECT_TOTAL, DATE_TODAY_FORMAT, DATE_TODAY_FORMAT,
                    DATE_TODAY_FORMAT, DATE_TODAY_FORMAT), BeanPropertyRowMapper.newInstance(BuyerProfit.class)
            );
        }
        return jdbcTemplate.queryForObject(String.format(SELECT_TOTAL, DATE_YESTERDAY_FORMAT, DATE_YESTERDAY_FORMAT,
                DATE_YESTERDAY_FORMAT, DATE_YESTERDAY_FORMAT), BeanPropertyRowMapper.newInstance(BuyerProfit.class)
        );
    }
}
