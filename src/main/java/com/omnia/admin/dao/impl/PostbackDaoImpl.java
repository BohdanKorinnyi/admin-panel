package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.PostbackDao;
import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.exception.QueryExecutionException;
import com.omnia.admin.model.BuyerPlan;
import com.omnia.admin.service.PostbackStats;
import com.omnia.admin.service.QueryHelper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

import static com.omnia.admin.grid.filter.FilterConstant.EMPTY;

@Log4j
@Repository
@AllArgsConstructor
public class PostbackDaoImpl implements PostbackDao {
    private static final String SELECT_POSTBACK = QueryHelper.loadQueryFromFile("postback.sql");
    private static final String SELECT_FULL_URL_BY_ID = "SELECT fullurl FROM postback WHERE id = ?;";

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
    public List<PostbackStats> getStats(StatisticFilter filter) {
        String buyerWhere = EMPTY;
        String dateWhere = EMPTY;
        if (!CollectionUtils.isEmpty(filter.getBuyers())) {
            StringJoiner joiner = new StringJoiner("','", "'", "'");
            filter.getBuyers().forEach(joiner::add);
            buyerWhere = " AND buyers.name IN (" + joiner.toString() + ") ";
        }
        if (!StringUtils.isEmpty(filter.getFrom()) && !StringUtils.isEmpty(filter.getTo())) {
            dateWhere = " AND date BETWEEN '" + filter.getFrom() + "' AND '" + filter.getTo() + "' ";
        }
        return jdbcTemplate.query(String.format(SELECT_POSTBACK, buyerWhere, dateWhere), BeanPropertyRowMapper.newInstance(PostbackStats.class));
    }
}
