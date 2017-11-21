package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.BuyerPlanDao;
import com.omnia.admin.model.BuyerPlan;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Month;
import java.util.List;
import java.util.StringJoiner;

@Log4j
@Repository
@AllArgsConstructor
public class BuyerPlanDaoImpl implements BuyerPlanDao {
    private static final String SELECT_BUYER_REVENUE_PLAN = "SELECT " +
            "  monthname(buyers_kpi.date)                                       AS 'month', " +
            "  buyers.name                                                      AS 'buyerName', " +
            "  catalog_kpi.name                                                 AS 'kpiName', " +
            "  buyers_kpi.kpi_value                                             AS 'kpiValue', " +
            "  CASE WHEN TRUNCATE(sum(revenue.sum), 2) IS NULL " +
            "    THEN TRUNCATE(0.0, 2) " +
            "  ELSE TRUNCATE(sum(revenue.sum), 2) END                           AS 'sum', " +
            "  CASE WHEN (revenue.sum / buyers_kpi.kpi_value) * 100 IS NULL " +
            "    THEN TRUNCATE(0.0, 2) " +
            "  ELSE TRUNCATE((revenue.sum / buyers_kpi.kpi_value) * 100, 2) END AS 'performance' " +
            "FROM buyers_kpi " +
            "  INNER JOIN catalog_kpi ON buyers_kpi.kpi_name = catalog_kpi.id " +
            "  INNER JOIN buyers ON buyers_kpi.buyer_id = buyers.id " +
            "  LEFT JOIN (SELECT " +
            "               buyers_kpi.id, " +
            "               MONTH(buyers_kpi.date) AS MONTH, " +
            "               sum(postback.sum / " +
            "                   (SELECT exchange.rate " +
            "                    FROM exchange " +
            "                    WHERE exchange.id = postback.exchange) * " +
            "                   (SELECT exchange.count " +
            "                    FROM exchange " +
            "                    WHERE exchange.id = postback.exchange) " +
            "               )                      AS sum " +
            "             FROM buyers_kpi " +
            "               INNER JOIN catalog_kpi ON buyers_kpi.kpi_name = catalog_kpi.id " +
            "               INNER JOIN postback ON MONTH(postback.date) = MONTH(buyers_kpi.date) " +
            "               INNER JOIN buyers ON buyers_kpi.buyer_id = buyers.id " +
            "               INNER JOIN affiliates ON buyers.id = affiliates.buyer_id " +
            "               INNER JOIN adverts ON adverts.advname = postback.advname " +
            "               INNER JOIN adv_status ON adverts.id = adv_status.adv_id " +
            "             WHERE catalog_kpi.name = 'Revenue' AND postback.afid = affiliates.afid AND adv_status.name = 'approved' " +
            "               %s " +
            "             GROUP BY MONTH(buyers_kpi.date), postback.currency) AS revenue ON buyers_kpi.id = revenue.id " +
            "WHERE catalog_kpi.name = 'Revenue' %s " +
            "GROUP BY buyers_kpi.buyer_id, MONTH(buyers_kpi.date) " +
            "ORDER BY MONTH(buyers_kpi.date) ASC;";
    private static final String SELECT_BUYER_PROFIT_PLAN = "SELECT " +
            "  buyers_kpi.id, " +
            "  monthname(buyers_kpi.date) AS 'month', " +
            "  buyers.name                AS 'buyerName', " +
            "  catalog_kpi.name           AS 'kpiName', " +
            "  buyers_kpi.kpi_value       AS 'kpiValue', " +
            "  CASE " +
            "  WHEN sum(profit.sum) IS NULL " +
            "    THEN 0.0 " +
            "  ELSE sum(profit.sum) END   AS 'sum', " +
            "  CASE " +
            "  WHEN profit.currency IS NULL " +
            "    THEN 'USD' " +
            "  ELSE profit.currency END   AS 'currency' " +
            "FROM buyers_kpi " +
            "  INNER JOIN catalog_kpi ON buyers_kpi.kpi_name = catalog_kpi.id " +
            "  INNER JOIN buyers ON buyers_kpi.buyer_id = buyers.id " +
            "  LEFT JOIN (SELECT " +
            "               buyers_kpi.id                      AS 'id', " +
            "               monthname(buyers_kpi.date)         AS 'month', " +
            "               sum(source_statistics_today.spent) AS 'sum', " +
            "               'USD'                              AS 'currency' " +
            "             FROM buyers_kpi " +
            "               INNER JOIN catalog_kpi ON buyers_kpi.kpi_name = catalog_kpi.id " +
            "               INNER JOIN source_statistics_today ON source_statistics_today.buyer_id = buyers_kpi.buyer_id " +
            "             WHERE catalog_kpi.name = 'Profit' AND MONTH(source_statistics_today.date) = MONTH(buyers_kpi.date) %s " +
            "             GROUP BY MONTH(buyers_kpi.date) " +
            "             UNION (SELECT " +
            "                      buyers_kpi.id                AS 'id', " +
            "                      monthname(buyers_kpi.date)   AS 'month', " +
            "                      sum(source_statistics.spent) AS 'sum', " +
            "                      'USD'                        AS 'currency' " +
            "                    FROM buyers_kpi " +
            "                      INNER JOIN catalog_kpi ON buyers_kpi.kpi_name = catalog_kpi.id " +
            "                      INNER JOIN source_statistics ON source_statistics.buyer_id = buyers_kpi.buyer_id " +
            "                    WHERE catalog_kpi.name = 'Profit' AND MONTH(source_statistics.date) = MONTH(buyers_kpi.date) %s " +
            "                    GROUP BY MONTH(buyers_kpi.date)) " +
            "             UNION (SELECT " +
            "                      buyers_kpi.id              AS 'id', " +
            "                      monthname(buyers_kpi.date) AS 'month', " +
            "                      sum(expenses.sum)          AS 'sum', " +
            "                      'USD'                      AS 'currency' " +
            "                    FROM buyers_kpi " +
            "                      INNER JOIN catalog_kpi ON buyers_kpi.kpi_name = catalog_kpi.id " +
            "                      INNER JOIN expenses ON expenses.buyer_id = buyers_kpi.buyer_id " +
            "                    WHERE catalog_kpi.name = 'Profit' AND MONTH(expenses.date) = MONTH(buyers_kpi.date) %s " +
            "                    GROUP BY MONTH(buyers_kpi.date))) AS profit ON profit.id = buyers_kpi.id " +
            "WHERE catalog_kpi.name = 'Profit' %s " +
            "GROUP BY buyers_kpi.buyer_id, MONTH(buyers_kpi.date) ORDER BY MONTH(buyers_kpi.date) ASC;";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<BuyerPlan> getBuyerRevenuePlan(List<String> buyers, List<String> month) {
        String whereClause = "";
        if (!CollectionUtils.isEmpty(buyers)) {
            whereClause += getBuyers(buyers);
        }
        if (!CollectionUtils.isEmpty(month)) {
            whereClause += getMonth(month);
        }
        return jdbcTemplate.query(String.format(SELECT_BUYER_REVENUE_PLAN, whereClause, whereClause), BeanPropertyRowMapper.newInstance(BuyerPlan.class));
    }


    @Override
    public List<BuyerPlan> getBuyerProfitPlan(List<String> buyers, List<String> month) {
        String sql = String.format(SELECT_BUYER_PROFIT_PLAN, getWhereClause(buyers, month), getWhereClause(buyers, month),
                getWhereClause(buyers, month), getWhereClause(buyers, month)
        );
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(BuyerPlan.class));
    }

    private String getWhereClause(List<String> buyers, List<String> months) {
        String whereClause = "";
        if (!CollectionUtils.isEmpty(buyers)) {
            whereClause += getBuyers(buyers);
        }
        if (!CollectionUtils.isEmpty(months)) {
            whereClause += getMonth(months);
        }
        return whereClause;
    }

    private String getMonth(List<String> months) {
        StringJoiner joiner = new StringJoiner("','", "'", "'");
        months.forEach(month -> {
            int value = Month.valueOf(month.toUpperCase()).getValue();
            joiner.add(String.valueOf(value));
        });
        return " AND MONTH(buyers_kpi.date) IN(" + joiner.toString() + ")";
    }

    private String getBuyers(List<String> buyers) {
        StringJoiner joiner = new StringJoiner("','", "'", "'");
        buyers.forEach(joiner::add);
        return " AND buyers_kpi.buyer_id IN(" + joiner.toString() + ")";
    }
}
