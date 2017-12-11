package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.SpentDao;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Float calculateBuyerAllTimeSpent(Integer buyerId) {
        return null;
    }

    @Override
    public Float calculateBuyerCurrentMonthSpent(Integer buyerId) {
        return jdbcTemplate.queryForObject(SELECT_CURRENT_MONTH_SPENT_BY_BUYER, Float.class, buyerId, buyerId, buyerId);
    }

    @Override
    public Float calculateBuyerCustomRangeSpent(Integer buyerId) {
        return null;
    }
}
