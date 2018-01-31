package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.BalanceDao;
import com.omnia.admin.model.Balance;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class BalanceDaoImpl implements BalanceDao {
    private static final String SELECT_BALANCE_PER_MONTH_AND_ADVERTISER = "SELECT " +
            "  TRUNCATE(sum(postback.sum / " +
            "               (SELECT exchange.rate " +
            "                FROM exchange " +
            "                WHERE exchange.id = postback.exchange) * " +
            "               (SELECT exchange.count " +
            "                FROM exchange " +
            "                WHERE exchange.id = postback.exchange)), 2) AS 'revenue', " +
            "  postback.advname, " +
            "  monthname(postback.date)                                  AS 'month', " +
            "  CASE WHEN adverts_income.sum_total IS NULL THEN 0 " +
            "  ELSE sum(adverts_income.sum_total) " +
            "  END                                                       AS 'income' " +
            "FROM postback " +
            "  INNER JOIN affiliates ON affiliates.afid = postback.afid " +
            "  INNER JOIN buyers ON affiliates.buyer_id = buyers.id " +
            "  INNER JOIN adverts ON adverts.advshortname = postback.advname " +
            "  INNER JOIN adv_status ON adv_status.adv_id = adverts.id " +
            "  INNER JOIN adverts_income ON adverts.id = adverts_income.adv_id " +
            "WHERE (postback.duplicate != 'FULL' OR postback.duplicate IS NULL) AND " +
            "      adv_status.real_status = 'approved' AND postback.status = adv_status.name AND postback.sum != 0 " +
            "      AND month(postback.date) = :month AND year(postback.date) = :year AND " +
            "      IF(concat(:advertiserIds) IS NULL, TRUE, buyers.id IN (:advertiserIds)) " +
            "GROUP BY month(postback.date), adverts.advshortname " +
            "ORDER BY adverts.advshortname ASC;";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Balance> getMonthBalance(int year, String month, List<Long> advertiserIds) {
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("year", year);
        source.addValue("month", month);
        source.addValue("advertiserIds", nullIfEmpty(advertiserIds));
        return namedParameterJdbcTemplate.query(SELECT_BALANCE_PER_MONTH_AND_ADVERTISER, source,
                BeanPropertyRowMapper.newInstance(Balance.class));
    }
}
