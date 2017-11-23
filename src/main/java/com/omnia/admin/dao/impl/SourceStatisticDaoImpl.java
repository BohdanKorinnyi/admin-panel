package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.SourceStatisticDao;
import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.Source;
import com.omnia.admin.service.QueryHelper;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.StringJoiner;

import static com.omnia.admin.grid.filter.FilterConstant.EMPTY;

@Repository
@AllArgsConstructor
public class SourceStatisticDaoImpl implements SourceStatisticDao {

    private static final String SELECT_STATISTIC = QueryHelper.loadQueryFromFile("statistic.sql");
    private static final String SELECT_DAILY_STATISTIC = QueryHelper.loadQueryFromFile("statistic_daily.sql");
    private static final String SELECT_PROFIT = "SELECT sum(result.sum) AS profit " +
            "FROM (SELECT sum(spent) AS sum " +
            "      FROM source_statistics_today " +
            "        INNER JOIN affiliates ON affiliates.afid = source_statistics_today.afid " +
            "      WHERE affiliates.buyer_id = ? AND month(source_statistics_today.date) = month(now()) " +
            "      UNION (SELECT sum(spent) AS sum " +
            "             FROM source_statistics " +
            "               INNER JOIN affiliates ON affiliates.afid = source_statistics.afid " +
            "             WHERE affiliates.buyer_id = ? AND month(source_statistics.date) = month(now())) " +
            "      UNION (SELECT sum(expenses.sum) AS sum " +
            "             FROM expenses " +
            "             WHERE expenses.buyer_id = ? AND month(expenses.date) = month(now()))) AS result;";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Source> getStatistics(StatisticFilter filter) {
        return jdbcTemplate.query(updateWhereClause(SELECT_STATISTIC, filter), BeanPropertyRowMapper.newInstance(Source.class));
    }

    @Override
    public List<Source> getDailyStatistics(StatisticFilter filter) {
        return jdbcTemplate.query(updateWhereClause(SELECT_DAILY_STATISTIC, filter), BeanPropertyRowMapper.newInstance(Source.class));
    }

    @Override
    public Float getProfitByBuyerId(int buyerId) {
        return jdbcTemplate.queryForObject(SELECT_PROFIT, Float.class, buyerId);
    }

    private String updateWhereClause(String sql, StatisticFilter filter) {
        String where = EMPTY;
        if (!CollectionUtils.isEmpty(filter.getBuyers())) {
            StringJoiner joiner = new StringJoiner("','", "'", "'");
            filter.getBuyers().forEach(joiner::add);
            where += " AND buyers.name IN (" + joiner.toString() + ") ";
        }
        if (!CollectionUtils.isEmpty(filter.getTypes())) {
            StringJoiner joiner = new StringJoiner("','", "'", "'");
            filter.getTypes().forEach(joiner::add);
            where += " AND accounts.type IN (" + joiner.toString() + ") ";
        }
        if (!StringUtils.isEmpty(filter.getFrom()) && !StringUtils.isEmpty(filter.getTo())) {
            where += " AND date BETWEEN '" + filter.getFrom() + "' AND '" + filter.getTo() + "' ";
        }
        return String.format(sql, where);
    }
}
