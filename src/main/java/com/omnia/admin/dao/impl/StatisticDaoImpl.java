package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.StatisticDao;
import com.omnia.admin.dto.StatisticFilterDto;
import com.omnia.admin.model.Statistic;
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
public class StatisticDaoImpl implements StatisticDao {

    private static final String SELECT_STATISTIC = "SELECT  " +
            "  count(*)   AS amount,  " +
            "  sum(spent) AS spent,  " +
            "  source_statistics.buyer_id,  " +
            "  source_statistics.campaign_name,  " +
            "  accounts.type AS accountType,  " +
            "  accounts.username,  " +
            "  buyers.name,  " +
            "  source_statistics.date  " +
            "FROM source_statistics  " +
            "  LEFT JOIN accounts ON accounts.account_id = source_statistics.account_id  " +
            "  LEFT JOIN buyers ON source_statistics.buyer_id = buyers.id  " +
            " %s  " +
            "GROUP BY source_statistics.buyer_id, source_statistics.date, source_statistics.campaign_id  " +
            "ORDER BY source_statistics.date;";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Statistic> getStatistics(StatisticFilterDto filter) {
        return jdbcTemplate.query(updateWhereClause(SELECT_STATISTIC, filter), BeanPropertyRowMapper.newInstance(Statistic.class));
    }

    private String updateWhereClause(String sql, StatisticFilterDto filter) {
        String where = null;
        if (!CollectionUtils.isEmpty(filter.getBuyers())) {
            StringJoiner joiner = new StringJoiner("','", "'", "'");
            filter.getBuyers().forEach(joiner::add);
            where = " buyers.name IN (" + joiner.toString() + ") ";
        }
        if (!CollectionUtils.isEmpty(filter.getTypes())) {
            StringJoiner joiner = new StringJoiner("','", "'", "'");
            filter.getTypes().forEach(joiner::add);
            String types = " accountType IN (" + joiner.toString() + ") ";
            where = StringUtils.isEmpty(where) ? types : " AND " + types;
        }
        if (!StringUtils.isEmpty(filter.getFrom()) && !StringUtils.isEmpty(filter.getTo())) {
            String range = " source_statistics.date BETWEEN '" + filter.getFrom() + "' AND '" + filter.getTo() + "' ";
            where = StringUtils.isEmpty(where) ? range : " AND " + range;
        }
        return String.format(sql, StringUtils.isEmpty(where) ? EMPTY : " WHERE " + where);
    }
}
