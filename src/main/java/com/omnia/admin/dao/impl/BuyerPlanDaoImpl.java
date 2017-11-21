package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.BuyerPlanDao;
import com.omnia.admin.model.BuyerPlan;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Month;
import java.util.List;
import java.util.StringJoiner;

@Repository
@AllArgsConstructor
public class BuyerPlanDaoImpl implements BuyerPlanDao {
    private static final String SELECT_BUYER_REVENUE_PLAN = "SELECT " +
            "  buyers_kpi.id AS kpiId, " +
            "  month(buyers_kpi.date) AS month," +
            "  revenue.* " +
            "FROM buyers_kpi " +
            "  LEFT JOIN (SELECT " +
            "               buyers_kpi.id        AS id, " +
//            "               MONTH(postback.date) AS month, " +
            "               buyers.name          AS buyerName, " +
            "               catalog_kpi.name     AS kpiName, " +
            "               buyers_kpi.kpi_value AS kpiValue, " +
            "               sum(postback.sum)    AS sum, " +
            "               postback.currency    AS currency, " +
            "               buyers_kpi.execution AS execution " +
            "             FROM buyers_kpi " +
            "               INNER JOIN buyers ON buyers_kpi.buyer_id = buyers.id " +
            "               INNER JOIN affiliates ON buyers.id = affiliates.buyer_id " +
            "               INNER JOIN postback ON postback.afid = affiliates.afid " +
            "               INNER JOIN catalog_kpi ON buyers_kpi.kpi_name = catalog_kpi.id " +
            "             WHERE catalog_kpi.name = 'Revenue' AND MONTH(postback.date) = MONTH(buyers_kpi.date) %s " +
            "             GROUP BY buyers.id, MONTH(postback.date), postback.currency) AS revenue ON revenue.id = buyers_kpi.id " +
            " WHERE revenue.kpiName = 'Revenue' " +
            "ORDER BY buyers_kpi.id ASC;";
    private static final String SELECT_BUYER_PROFIT_PLAN = "SELECT " +
            "  buyers_kpi.id AS kpiId, " +
            "  month(buyers_kpi.date) AS month," +
            "  profit.* " +
            "FROM buyers_kpi " +
            "  LEFT JOIN (SELECT " +
            "               result.id, " +
//            "               result.month, " +
            "               result.buyerName, " +
            "               result.kpiName, " +
            "               result.kpiValue, " +
            "               result.currency, " +
            "               sum(result.sum) AS sum, " +
            "               result.execution " +
            "             FROM ((SELECT " +
            "                      buyers_kpi.id                 AS id, " +
            "                      MONTH(source_statistics.date) AS month, " +
            "                      buyers.name                   AS buyerName, " +
            "                      catalog_kpi.name              AS kpiName, " +
            "                      buyers_kpi.kpi_value          AS kpiValue, " +
            "                      sum(source_statistics.spent)  AS sum, " +
            "                      'USD'                         AS currency, " +
            "                      buyers_kpi.execution          AS execution " +
            "                    FROM buyers_kpi " +
            "                      LEFT JOIN source_statistics ON source_statistics.buyer_id = buyers_kpi.buyer_id " +
            "                      LEFT JOIN buyers ON buyers_kpi.buyer_id = buyers.id " +
            "                      LEFT JOIN catalog_kpi ON buyers_kpi.kpi_name = catalog_kpi.id " +
            "                    WHERE catalog_kpi.name = 'Profit' AND MONTH(buyers_kpi.date) = MONTH(source_statistics.date) %s " +
            "                    GROUP BY buyers.id, month) " +
            "                   UNION (SELECT " +
            "                            buyers_kpi.id                       AS id, " +
            "                            MONTH(source_statistics_today.date) AS month, " +
            "                            buyers.name                         AS buyerName, " +
            "                            catalog_kpi.name                    AS kpiName, " +
            "                            buyers_kpi.kpi_value                AS kpiValue, " +
            "                            sum(source_statistics_today.spent)  AS sum, " +
            "                            'USD'                               AS currency, " +
            "                            buyers_kpi.execution                AS execution " +
            "                          FROM buyers_kpi " +
            "                            LEFT JOIN source_statistics_today ON source_statistics_today.buyer_id = buyers_kpi.buyer_id " +
            "                            LEFT JOIN buyers ON buyers_kpi.buyer_id = buyers.id " +
            "                            LEFT JOIN catalog_kpi ON buyers_kpi.kpi_name = catalog_kpi.id " +
            "                          WHERE " +
            "                            catalog_kpi.name = 'Profit' AND MONTH(buyers_kpi.date) = MONTH(source_statistics_today.date) %s " +
            "                          GROUP BY buyers.id, month) " +
            "                   UNION (SELECT " +
            "                            buyers_kpi.id        AS id, " +
            "                            MONTH(expenses.date) AS month, " +
            "                            buyers.name          AS buyerName, " +
            "                            catalog_kpi.name     AS kpiName, " +
            "                            buyers_kpi.kpi_value AS kpiValue, " +
            "                            sum(expenses.sum)    AS sum, " +
            "                            'USD'                AS currency, " +
            "                            buyers_kpi.execution AS execution " +
            "                          FROM buyers_kpi " +
            "                            LEFT JOIN expenses ON expenses.buyer_id = buyers_kpi.buyer_id " +
            "                            LEFT JOIN buyers ON buyers_kpi.buyer_id = buyers.id " +
            "                            LEFT JOIN catalog_kpi ON buyers_kpi.kpi_name = catalog_kpi.id " +
            "                          WHERE catalog_kpi.name = 'Profit' AND MONTH(buyers_kpi.date) = MONTH(expenses.date) %s " +
            "                          GROUP BY buyers.id, month)) AS result " +
            "             GROUP BY result.buyerName, result.month) AS profit ON profit.id = buyers_kpi.id " +
            " WHERE profit.kpiName = 'Profit';";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<BuyerPlan> getBuyerProfitPlan(List<String> buyers, List<String> month) {
        return jdbcTemplate.query(
                String.format(
                        SELECT_BUYER_PROFIT_PLAN,
                        getWhereClause(buyers, month, "source_statistics"),
                        getWhereClause(buyers, month, "source_statistics_today"),
                        getWhereClause(buyers, month, "expenses")
                ),
                BeanPropertyRowMapper.newInstance(BuyerPlan.class)
        );
    }

    @Override
    public List<BuyerPlan> getBuyerRevenuePlan(List<String> buyers, List<String> month) {
        String whereClause = "";
        if (!CollectionUtils.isEmpty(buyers)) {
            whereClause += getBuyers(buyers);
        }
        if (!StringUtils.isEmpty(month)) {
            whereClause += getMonth(month, "postback");
        }
        return jdbcTemplate.query(String.format(SELECT_BUYER_REVENUE_PLAN, whereClause), BeanPropertyRowMapper.newInstance(BuyerPlan.class));
    }

    private String getWhereClause(List<String> buyers, List<String> months, String table) {
        String whereClause = "";
        if (!CollectionUtils.isEmpty(buyers)) {
            whereClause += getBuyers(buyers);
        }
        if (!StringUtils.isEmpty(months)) {
            whereClause += getMonth(months, table);
        }
        return whereClause;
    }

    private String getMonth(List<String> months, String table) {
        StringJoiner joiner = new StringJoiner("','", "'", "'");
        months.forEach(month -> {
            int value = Month.valueOf(month.toUpperCase()).getValue();
            joiner.add(String.valueOf(value));
        });
        return " AND MONTH(" + table + ".date) IN(" + joiner.toString() + ")";
    }

    private String getBuyers(List<String> buyers) {
        StringJoiner joiner = new StringJoiner("','", "'", "'");
        buyers.forEach(joiner::add);
        return " AND buyers.name IN(" + joiner.toString() + ")";
    }
}
