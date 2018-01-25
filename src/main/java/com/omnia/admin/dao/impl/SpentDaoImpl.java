package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.SpentDao;
import com.omnia.admin.model.BuyerCosts;
import com.omnia.admin.model.Spent;
import com.omnia.admin.service.QueryHelper;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
@AllArgsConstructor
public class SpentDaoImpl implements SpentDao {

    private static final String SELECT_SPENT_BY_BUYER = QueryHelper.loadQueryFromFile("spent_by_buyer.sql");
    private static final String SELECT_CURRENT_MONTH_SPENT_BY_BUYER = "SELECT truncate(sum(result.spent), 2) AS 'spent' " +
            "FROM (SELECT sum(expenses.sum) AS 'spent' " +
            "      FROM expenses " +
            "        INNER JOIN buyers ON expenses.buyer_id = buyers.id " +
            "      WHERE expenses.sum != 0 AND month(expenses.date) = month(now()) AND buyers.id = ? " +
            "      UNION (SELECT sum(source_statistics.spent) AS 'spent' " +
            "             FROM source_statistics " +
            "               INNER JOIN affiliates ON affiliates.afid = source_statistics.afid " +
            "               INNER JOIN buyers ON affiliates.buyer_id = buyers.id " +
            "             WHERE source_statistics.spent != 0 AND month(source_statistics.date) = month(now()) AND buyers.id = ?) " +
            "      UNION (SELECT sum(source_statistics_today.spent) AS 'spent' " +
            "             FROM source_statistics_today " +
            "               INNER JOIN affiliates ON affiliates.afid = source_statistics_today.afid " +
            "               INNER JOIN buyers ON affiliates.buyer_id = buyers.id " +
            "             WHERE source_statistics_today.spent != 0 AND source_statistics_today.date = date(now()) AND buyers.id = ?)) AS result;";
    private static final String SELECT_TODAY_SPENT = "SELECT truncate(sum(result.spent), 2) AS 'spent' " +
            "            FROM (SELECT sum(expenses.sum) AS 'spent' " +
            "                  FROM expenses " +
            "                    INNER JOIN buyers ON expenses.buyer_id = buyers.id  " +
            "                  WHERE expenses.sum != 0 AND expenses.date = date(now()) AND buyers.id = ? " +
            "                  UNION (SELECT sum(source_statistics.spent) AS 'spent'  " +
            "                         FROM source_statistics " +
            "                           INNER JOIN affiliates ON affiliates.afid = source_statistics.afid " +
            "                           INNER JOIN buyers ON affiliates.buyer_id = buyers.id  " +
            "                         WHERE source_statistics.spent != 0 AND source_statistics.date = date(now()) AND buyers.id = ?) " +
            "                  UNION (SELECT sum(source_statistics_today.spent) AS 'spent' " +
            "                         FROM source_statistics_today " +
            "                           INNER JOIN affiliates ON affiliates.afid = source_statistics_today.afid " +
            "                           INNER JOIN buyers ON affiliates.buyer_id = buyers.id " +
            "                         WHERE source_statistics_today.spent != 0 AND source_statistics_today.date = date(now()) AND buyers.id = ?)) AS result;";
    private static final String SELECT_YESTERDAY_SPENT = "SELECT truncate(sum(result.spent), 2) AS 'spent'  " +
            "            FROM (SELECT sum(expenses.sum) AS 'spent'  " +
            "                  FROM expenses " +
            "                    INNER JOIN buyers ON expenses.buyer_id = buyers.id  " +
            "                  WHERE expenses.sum != 0 AND expenses.date = date(now() - INTERVAL 1 DAY) AND buyers.id = ? " +
            "                  UNION (SELECT sum(source_statistics.spent) AS 'spent'  " +
            "                         FROM source_statistics " +
            "                           INNER JOIN affiliates ON affiliates.afid = source_statistics.afid  " +
            "                           INNER JOIN buyers ON affiliates.buyer_id = buyers.id  " +
            "                         WHERE source_statistics.spent != 0 AND source_statistics.date = date(now() - INTERVAL 1 DAY) AND buyers.id = ?)  " +
            "                  UNION (SELECT sum(source_statistics_today.spent) AS 'spent'  " +
            "                         FROM source_statistics_today " +
            "                           INNER JOIN affiliates ON affiliates.afid = source_statistics_today.afid  " +
            "                           INNER JOIN buyers ON affiliates.buyer_id = buyers.id " +
            "                         WHERE source_statistics_today.spent != 0 AND source_statistics_today.date = date(now() - INTERVAL 1 DAY) AND buyers.id = ?)) AS result;";

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Spent> getSpentByBuyerAndYear(int buyer, int year) {
        return jdbcTemplate.query("SELECT " +
                "  truncate(sum(result.spent), 2) AS 'value', " +
                "  monthname(result.date)         AS 'date' " +
                "FROM (SELECT " +
                "        sum(expenses.sum) AS 'spent', " +
                "        expenses.date " +
                "      FROM expenses " +
                "        INNER JOIN buyers ON expenses.buyer_id = buyers.id " +
                "      WHERE expenses.sum != 0 AND buyers.id = ? AND year(expenses.date) = ? " +
                "      GROUP BY monthname(expenses.date) " +
                "      UNION (SELECT " +
                "               sum(source_statistics.spent) AS 'spent', " +
                "               source_statistics.date " +
                "             FROM source_statistics " +
                "               INNER JOIN affiliates ON affiliates.afid = source_statistics.afid " +
                "               INNER JOIN buyers ON affiliates.buyer_id = buyers.id " +
                "             WHERE source_statistics.spent != 0 AND buyers.id = ? AND year(source_statistics.date) = ? " +
                "             GROUP BY monthname(source_statistics.date) " +
                "      ) " +
                "      UNION (SELECT " +
                "               sum(source_statistics_today.spent) AS 'spent', " +
                "               source_statistics_today.date " +
                "             FROM source_statistics_today " +
                "               INNER JOIN affiliates ON affiliates.afid = source_statistics_today.afid " +
                "               INNER JOIN buyers ON affiliates.buyer_id = buyers.id " +
                "             WHERE source_statistics_today.spent != 0 AND buyers.id = ? AND year(source_statistics_today.date) = ? " +
                "             GROUP BY monthname(source_statistics_today.date) " +
                "      )) AS result " +
                "GROUP BY monthname(result.date) " +
                "ORDER BY month(result.date);", BeanPropertyRowMapper.newInstance(Spent.class), buyer, year, buyer, year, buyer, year);
    }

    @Override
    public Float calculateBuyerCurrentMonthSpent(Integer buyerId) {
        return jdbcTemplate.queryForObject(SELECT_CURRENT_MONTH_SPENT_BY_BUYER, Float.class, buyerId, buyerId, buyerId);
    }

    @Override
    public Float calculateBuyerTodaySpent(Integer buyerId) {
        return jdbcTemplate.queryForObject(SELECT_TODAY_SPENT, Float.class, buyerId, buyerId, buyerId);
    }

    @Override
    public Float calculateBuyerYesterdaySpent(Integer buyerId) {
        return jdbcTemplate.queryForObject(SELECT_YESTERDAY_SPENT, Float.class, buyerId, buyerId, buyerId);
    }

    @Override
    public List<BuyerCosts> getSpentReport(List<Integer> buyerIds, List<String> sources, String from, String to) {
        MapSqlParameterSource filters = new MapSqlParameterSource();
        filters.addValue("buyers", CollectionUtils.isEmpty(buyerIds) ? null : buyerIds);
        filters.addValue("sources", CollectionUtils.isEmpty(sources) ? null : sources);
        filters.addValue("from", from);
        filters.addValue("to", to);
        return namedParameterJdbcTemplate.query(SELECT_SPENT_BY_BUYER, filters, BeanPropertyRowMapper.newInstance(BuyerCosts.class));
    }
}
