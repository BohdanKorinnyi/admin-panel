package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.PostbackDao;
import com.omnia.admin.exception.QueryExecutionException;
import com.omnia.admin.model.Postback;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Log4j
@Repository
@AllArgsConstructor
public class PostbackDaoImpl implements PostbackDao {
    private static final String SELECT_FULL_URL_BY_ID = "SELECT fullurl FROM postback WHERE id = ?;";
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
            "  INNER JOIN adverts ON adverts.advname = postback.advname " +
            "  INNER JOIN adv_status ON adv_status.adv_id = adverts.id " +
            "WHERE " +
            "  adv_status.real_status = 'approved' AND postback.status = adv_status.name AND postback.sum != 0 AND buyers.id = ? AND month(postback.date) = month(now())";

    private static final String POSTBACK_BY_CONVERSION = "SELECT postback.* " +
            "FROM conversions_postback " +
            "  INNER JOIN postback ON conversions_postback.postback_id = postback.id " +
            "WHERE conversions_postback.conversion_id = ?;";

    private final JdbcTemplate jdbcTemplate;

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
        return jdbcTemplate.queryForObject(SELECT_BUYER_REVENUE, Float.class, buyerId);
    }

    @Override
    public List<Postback> findPostbackByConversionId(long conversionId) {
        return jdbcTemplate.query(POSTBACK_BY_CONVERSION, BeanPropertyRowMapper.newInstance(Postback.class), conversionId);
    }
}
