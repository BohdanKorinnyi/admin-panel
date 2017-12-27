package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.AdvertsIncomeDao;
import com.omnia.admin.model.AdvertiserIncome;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
@AllArgsConstructor
public class AdvertsIncomeDaoImpl implements AdvertsIncomeDao {
    private static final String SELECT_TOTAL_PAID = "SELECT truncate(sum(sum_total), 2) FROM adverts_income WHERE adverts_income.date BETWEEN :from AND :to";
    private static final String SELECT_TOTAL_PAID_GROUPED_BY_ADVERTISER = "SELECT " +
            "  adverts.id, " +
            "  adverts.advshortname                                      AS 'advertiser', " +
            "  TRUNCATE(sum(postback.sum / " +
            "               (SELECT exchange.rate " +
            "                FROM exchange " +
            "                WHERE exchange.id = postback.exchange) * " +
            "               (SELECT exchange.count " +
            "                FROM exchange " +
            "                WHERE exchange.id = postback.exchange)), 2) AS 'revenue', " +
            "  income_data.income                                        AS 'income' " +
            "FROM postback " +
            "  INNER JOIN affiliates ON affiliates.afid = postback.afid " +
            "  INNER JOIN buyers ON affiliates.buyer_id = buyers.id " +
            "  INNER JOIN adverts ON adverts.advshortname = postback.advname " +
            "  INNER JOIN adv_status ON adv_status.adv_id = adverts.id " +
            "  LEFT JOIN (SELECT " +
            "               adv_id, " +
            "               sum(sum_total) AS 'income' " +
            "             FROM adverts_income " +
            "             WHERE adverts_income.date BETWEEN :from AND :to AND IF(concat(:advertiserIds) IS NULL, TRUE, adv_id IN (:advertiserIds)) " +
            "             GROUP BY adv_id) AS income_data ON income_data.adv_id = adverts.id " +
            "WHERE (postback.duplicate != 'FULL' OR postback.duplicate IS NULL) AND " +
            "      adv_status.real_status = 'approved' AND postback.status = adv_status.name AND postback.sum != 0 " +
            "      AND postback.date BETWEEN :from AND :to AND IF(concat(:advertiserIds) IS NULL, TRUE, adverts.id IN (:advertiserIds)) " +
            "GROUP BY adverts.id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Float getTotalByPeriod(List<Integer> advertiserIds, String from, String to) {
        MapSqlParameterSource filter = getAdvertsIncomeFilter(advertiserIds, from, to);
        return namedParameterJdbcTemplate.queryForObject(SELECT_TOTAL_PAID, filter, Float.class);
    }

    @Override
    public List<AdvertiserIncome> getIncomeByPeriodGroupedByAdvertiser(List<Integer> advertiserIds, String from, String to) {
        MapSqlParameterSource filter = getAdvertsIncomeFilter(advertiserIds, from, to);
        return namedParameterJdbcTemplate.query(SELECT_TOTAL_PAID_GROUPED_BY_ADVERTISER, filter, BeanPropertyRowMapper.newInstance(AdvertiserIncome.class));
    }

    private MapSqlParameterSource getAdvertsIncomeFilter(List<Integer> advertiserIds, String from, String to) {
        MapSqlParameterSource filter = new MapSqlParameterSource();
        filter.addValue("advertiserIds", CollectionUtils.isEmpty(advertiserIds) ? null : advertiserIds);
        filter.addValue("from", from);
        filter.addValue("to", to);
        return filter;
    }
}
