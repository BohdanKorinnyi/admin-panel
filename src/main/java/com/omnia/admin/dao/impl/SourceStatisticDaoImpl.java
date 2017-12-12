package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.SourceStatisticDao;
import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.Source;
import com.omnia.admin.model.SourceStat;
import com.omnia.admin.service.QueryHelper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.StringJoiner;

import static com.omnia.admin.grid.filter.FilterConstant.EMPTY;

@Log4j
@Repository
@AllArgsConstructor
public class SourceStatisticDaoImpl implements SourceStatisticDao {

    private static final String SELECT_STATISTIC = QueryHelper.loadQueryFromFile("statistic.sql");
    private static final String SELECT_DETAILED_SOURCE_STATISTIC = QueryHelper.loadQueryFromFile("detailed_source_statistic.sql");
    private static final String SELECT_SOURCE_STATISTIC = QueryHelper.loadQueryFromFile("source_statistic.sql");
    private static final String SELECT_DAILY_STATISTIC = QueryHelper.loadQueryFromFile("statistic_daily.sql");
    private static final String SELECT_PROFIT = "SELECT sum(result.sum) AS profit " +
            "FROM (SELECT sum(spent) AS sum " +
            "      FROM source_statistics_today " +
            "        INNER JOIN affiliates ON affiliates.afid = source_statistics_today.afid " +
            "      WHERE affiliates.buyer_id = ? AND source_statistics_today.date = date(now()) " +
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
        String sql = updateWhereClause(SELECT_STATISTIC, filter);
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Source.class));
    }

    @Override
    public List<Source> getDailyStatistics(StatisticFilter filter) {
        return jdbcTemplate.query(updateWhereClause(SELECT_DAILY_STATISTIC, filter), BeanPropertyRowMapper.newInstance(Source.class));
    }

    @Override
    public Float getProfitByBuyerId(int buyerId) {
        return jdbcTemplate.queryForObject(SELECT_PROFIT, Float.class, buyerId, buyerId, buyerId);
    }

    @Override
    public List<SourceStat> getSourceStat(List<Integer> buyerIds, String from, String to) {
        String buyerClause = EMPTY;
        if (!CollectionUtils.isEmpty(buyerIds)) {
            String ids = StringUtils.collectionToCommaDelimitedString(buyerIds);
            buyerClause = " AND buyers.id IN (" + ids + ")";
        }
        String dateClause1 = EMPTY;
        String dateClause2 = EMPTY;
        String dateClause3 = EMPTY;
        String dateClause4 = EMPTY;
        if (!StringUtils.isEmpty(from) && !StringUtils.isEmpty(to)) {
            dateClause1 = " AND expenses.date BETWEEN '" + from + "' AND '" + to + "'";
            dateClause2 = " AND source_statistics.date BETWEEN '" + from + "' AND '" + to + "'";
            dateClause3 = " AND source_statistics_today.date BETWEEN '" + from + "' AND '" + to + "'";
            dateClause4 = " AND postback.date BETWEEN '" + from + "' AND '" + to + "'";
        }
        return jdbcTemplate.query(
                String.format(SELECT_SOURCE_STATISTIC,
                        buyerClause + dateClause1,
                        buyerClause + dateClause2,
                        buyerClause + dateClause3,
                        buyerClause + dateClause4
                ),
                BeanPropertyRowMapper.newInstance(SourceStat.class)
        );
    }

    @Override
    public List<SourceStat> getSourceStatByDate(Integer buyerId, String date) {
        return jdbcTemplate.query(SELECT_DETAILED_SOURCE_STATISTIC, BeanPropertyRowMapper.newInstance(SourceStat.class),
                date, buyerId, date, buyerId, buyerId, date, buyerId
        );
    }

    private String updateWhereClause(String sql, StatisticFilter filter) {
        String where = EMPTY;
        if (!CollectionUtils.isEmpty(filter.getBuyers())) {
            where += " AND buyers.id IN (" + StringUtils.collectionToCommaDelimitedString(filter.getBuyers()) + ") ";
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
