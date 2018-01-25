package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.PostbackDao;
import com.omnia.admin.exception.QueryExecutionException;
import com.omnia.admin.model.Postback;
import com.omnia.admin.model.Revenue;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Log4j
@Repository
@AllArgsConstructor
public class PostbackDaoImpl implements PostbackDao {
    private static final String SELECT_FULL_URL_BY_ID = "SELECT fullurl FROM postback WHERE id = ?;";
    private static final String CURRENT_MONTH_INTERVAL = " AND month(postback.date) = month(now()) ";
    private static final String TODAY_INTERVAL = " AND postback.date = date(now()) ";
    private static final String YESTERDAY_INTERVAL = " AND postback.date = date(now() - INTERVAL 1 DAY) ";
    private static final String BY_DATE_RANGE = " AND postback.date BETWEEN :from AND :to ";
    private static final String BY_ADVERTISER_IDS = " AND IF(concat(:advertiserIds) IS NULL, TRUE, adverts.id IN (:advertiserIds)) ";
    private static final String BY_BUYER_ID = " AND buyers.id = ? ";
    private static final String SELECT_BUYER_REVENUE = "SELECT " +
            "  TRUNCATE(sum(postback.sum / " +
            "               (SELECT exchange.rate " +
            "                FROM exchange " +
            "                WHERE exchange.id = postback.exchange) * " +
            "               (SELECT exchange.count " +
            "                FROM exchange " +
            "                WHERE exchange.id = postback.exchange)), 2) AS 'revenue'" +
            "FROM postback " +
            "  INNER JOIN affiliates ON affiliates.afid = postback.afid " +
            "  INNER JOIN buyers ON affiliates.buyer_id = buyers.id " +
            "  INNER JOIN adverts ON adverts.advshortname = postback.advname " +
            "  INNER JOIN adv_status ON adv_status.adv_id = adverts.id " +
            "WHERE (postback.duplicate != 'FULL' OR postback.duplicate IS NULL) AND" +
            "  adv_status.real_status = 'approved' AND postback.status = adv_status.name AND postback.sum != 0 ";
    private static final String POSTBACK_BY_CONVERSION = "SELECT postback.* " +
            "FROM conversions_postback " +
            "  INNER JOIN postback ON conversions_postback.postback_id = postback.id " +
            "WHERE conversions_postback.conversion_id = ?;";


    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Revenue> getRevenueByBuyerIdAndYear(long buyerId, int year) {
        return jdbcTemplate.query("SELECT " +
                        "  monthname(postback.date)                                  AS 'date', " +
                        "  TRUNCATE(sum(postback.sum / " +
                        "               (SELECT exchange.rate " +
                        "                FROM exchange " +
                        "                WHERE exchange.id = postback.exchange) * " +
                        "               (SELECT exchange.count " +
                        "                FROM exchange " +
                        "                WHERE exchange.id = postback.exchange)), 2) AS 'value' " +
                        "FROM postback " +
                        "  INNER JOIN affiliates ON affiliates.afid = postback.afid " +
                        "  INNER JOIN buyers ON affiliates.buyer_id = buyers.id " +
                        "  INNER JOIN adverts ON adverts.advshortname = postback.advname " +
                        "  INNER JOIN adv_status ON adv_status.adv_id = adverts.id " +
                        "WHERE (postback.duplicate != 'FULL' OR postback.duplicate IS NULL) AND " +
                        "      adv_status.real_status = 'approved' AND postback.status = adv_status.name AND postback.sum != 0 " +
                        "      AND buyers.id = ? AND year(postback.date) = ? GROUP BY monthname(postback.date) " +
                        "ORDER BY month(postback.date) ASC ",
                BeanPropertyRowMapper.newInstance(Revenue.class), buyerId, year);
    }

    @Override
    public Float getRevenueByPeriod(List<Integer> advertiserIds, String from, String to) {
        return namedParameterJdbcTemplate.queryForObject(SELECT_BUYER_REVENUE + BY_DATE_RANGE + BY_ADVERTISER_IDS,
                getAdvertsIncomeFilter(advertiserIds, from, to), Float.class);
    }

    @Override
    public Optional<String> findFullUrlById(Long postbackId) {
        try {
            String fullUrl = jdbcTemplate.queryForObject(SELECT_FULL_URL_BY_ID, String.class, postbackId);
            return Optional.of(fullUrl);
        } catch (EmptyResultDataAccessException e) {
            log.error("Not found fullurl for postbackId=" + postbackId);
            return Optional.empty();
        } catch (Exception e) {
            log.error("Not found fullurl for postbackId=" + postbackId, e);
            throw new QueryExecutionException(SELECT_FULL_URL_BY_ID);
        }
    }

    @Override
    public Float getRevenueByBuyer(int buyerId) {
        return jdbcTemplate.queryForObject(SELECT_BUYER_REVENUE + BY_BUYER_ID + CURRENT_MONTH_INTERVAL, Float.class, buyerId);
    }

    @Override
    public Float getTodayRevenueByBuyer(int buyerId) {
        return jdbcTemplate.queryForObject(SELECT_BUYER_REVENUE + BY_BUYER_ID + TODAY_INTERVAL, Float.class, buyerId);
    }

    @Override
    public Float getYesterdayRevenueByBuyer(int buyerId) {
        return jdbcTemplate.queryForObject(SELECT_BUYER_REVENUE + BY_BUYER_ID + YESTERDAY_INTERVAL, Float.class, buyerId);
    }

    @Override
    public List<Postback> findPostbackByConversionId(long conversionId) {
        return jdbcTemplate.query(POSTBACK_BY_CONVERSION, BeanPropertyRowMapper.newInstance(Postback.class), conversionId);
    }

    private MapSqlParameterSource getAdvertsIncomeFilter(List<Integer> advertiserIds, String from, String to) {
        MapSqlParameterSource filter = new MapSqlParameterSource();
        filter.addValue("advertiserIds", CollectionUtils.isEmpty(advertiserIds) ? null : advertiserIds);
        filter.addValue("from", from);
        filter.addValue("to", to);
        return filter;
    }
}
