package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.SpentDao;
import com.omnia.admin.model.BuyerCosts;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class SpentDaoImpl implements SpentDao {

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

    private static final String SELECT_SPENT_BY_BUYER = "SELECT " +
            "  truncate(sum(result.spent), 2) AS 'spent', " +
            "  result.source, " +
            "  result.buyerId, " +
            "  result.date " +
            "FROM (SELECT " +
            "        sum(expenses.sum)    AS 'spent', " +
            "        expenses.description AS 'source', " +
            "        expenses.buyer_id    AS 'buyerId', " +
            "        expenses.date        AS 'date' " +
            "      FROM expenses " +
            "        INNER JOIN buyers ON expenses.buyer_id = buyers.id " +
            "      WHERE expenses.sum != 0 AND month(expenses.date) = month(now()) " +
            "      GROUP BY expenses.date, expenses.description, buyers.id " +
            "      UNION (SELECT " +
            "               sum(source_statistics.spent) AS 'spent', " +
            "               accounts.name                AS 'source', " +
            "               buyers.id                    AS 'buyerId', " +
            "               source_statistics.date       AS 'date' " +
            "             FROM source_statistics " +
            "               INNER JOIN affiliates ON affiliates.afid = source_statistics.afid " +
            "               INNER JOIN buyers ON affiliates.buyer_id = buyers.id " +
            "               INNER JOIN accounts ON source_statistics.account_id = accounts.account_id " +
            "             WHERE source_statistics.spent != 0 AND month(source_statistics.date) = month(now()) " +
            "             GROUP BY source_statistics.date, source_statistics.account_id, buyers.id) " +
            "      UNION (SELECT " +
            "               sum(source_statistics_today.spent) AS 'spent', " +
            "               accounts.name                      AS 'source', " +
            "               buyers.id                          AS 'buyerId', " +
            "               source_statistics_today.date       AS 'date' " +
            "             FROM source_statistics_today " +
            "               INNER JOIN affiliates ON affiliates.afid = source_statistics_today.afid " +
            "               INNER JOIN buyers ON affiliates.buyer_id = buyers.id " +
            "               INNER JOIN accounts ON source_statistics_today.account_id = accounts.account_id " +
            "             WHERE source_statistics_today.spent != 0 AND source_statistics_today.date = date(now()) " +
            "             GROUP BY source_statistics_today.date, source_statistics_today.account_id, buyers.id) " +
            "     ) AS result " +
            "GROUP BY result.date, result.source, result.buyerId " +
            "ORDER BY result.date DESC;";

    private final JdbcTemplate jdbcTemplate;

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
    public List<BuyerCosts> createSpentByBuyerReport(String from, String to) {
        return jdbcTemplate.query(SELECT_SPENT_BY_BUYER, BeanPropertyRowMapper.newInstance(BuyerCosts.class));
    }
}
