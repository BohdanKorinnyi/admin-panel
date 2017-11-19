package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.BuyerPlanDao;
import com.omnia.admin.model.BuyerPlan;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class BuyerPlanDaoImpl implements BuyerPlanDao {
    private static final String SELECT_BUYER_REVENUE_PLAN = "SELECT " +
            "  MONTH(postback.date) AS month, " +
            "  buyers.name          AS buyerName, " +
            "  catalog_kpi.name     AS kpiName, " +
            "  buyers_kpi.kpi_value AS kpiValue, " +
            "  sum(postback.sum)    AS sum, " +
            "  postback.currency    AS currency, " +
            "  buyers_kpi.execution AS execution " +
            "FROM buyers_kpi " +
            "  INNER JOIN buyers ON buyers_kpi.buyer_id = buyers.id " +
            "  INNER JOIN affiliates ON buyers.id = affiliates.buyer_id " +
            "  INNER JOIN postback ON postback.afid = affiliates.afid " +
            "  INNER JOIN catalog_kpi ON buyers_kpi.kpi_name = catalog_kpi.id " +
            "WHERE catalog_kpi.name = 'Revenue' " +
            "GROUP BY buyers.id, MONTH(postback.date), postback.currency;";
    private static final String SELECT_BUYER_PROFIT_PLAN = "SELECT " +
            "  result.month, " +
            "  result.buyerName, " +
            "  result.kpiName, " +
            "  result.kpiValue, " +
            "  sum(result.sum) AS sum, " +
            "  result.execution " +
            "FROM ( " +
            "       (SELECT " +
            "          MONTH(source_statistics.date) AS month, " +
            "          buyers.name                   AS buyerName, " +
            "          catalog_kpi.name              AS kpiName, " +
            "          buyers_kpi.kpi_value          AS kpiValue, " +
            "          sum(source_statistics.spent)  AS sum, " +
            "          buyers_kpi.execution          AS execution " +
            "        FROM buyers_kpi " +
            "          INNER JOIN source_statistics ON source_statistics.buyer_id = buyers_kpi.buyer_id " +
            "          INNER JOIN buyers ON source_statistics.buyer_id = buyers.id " +
            "          INNER JOIN catalog_kpi ON buyers_kpi.kpi_name = catalog_kpi.id " +
            "        WHERE catalog_kpi.name = 'Profit' " +
            "        GROUP BY buyers.id, month) " +
            "       UNION (SELECT " +
            "                MONTH(source_statistics_today.date) AS month, " +
            "                buyers.name                         AS buyerName, " +
            "                catalog_kpi.name                    AS kpiName, " +
            "                buyers_kpi.kpi_value                AS kpiValue, " +
            "                sum(source_statistics_today.spent)  AS sum, " +
            "                buyers_kpi.execution                AS execution " +
            "              FROM buyers_kpi " +
            "                INNER JOIN source_statistics_today ON source_statistics_today.buyer_id = buyers_kpi.buyer_id " +
            "                INNER JOIN buyers ON source_statistics_today.buyer_id = buyers.id " +
            "                INNER JOIN catalog_kpi ON buyers_kpi.kpi_name = catalog_kpi.id " +
            "              WHERE catalog_kpi.name = 'Profit' " +
            "              GROUP BY buyers.id, month) " +
            "       UNION (SELECT " +
            "                MONTH(expenses.date) AS month, " +
            "                buyers.name          AS buyerName, " +
            "                catalog_kpi.name     AS kpiName, " +
            "                buyers_kpi.kpi_value AS kpiValue, " +
            "                sum(expenses.sum)    AS sum, " +
            "                buyers_kpi.execution AS execution " +
            "              FROM buyers_kpi " +
            "                INNER JOIN expenses ON expenses.buyer_id = buyers_kpi.buyer_id " +
            "                INNER JOIN buyers ON expenses.buyer_id = buyers.id " +
            "                INNER JOIN catalog_kpi ON buyers_kpi.kpi_name = catalog_kpi.id " +
            "              WHERE catalog_kpi.name = 'Profit' " +
            "              GROUP BY buyers.id, month)) AS result " +
            "GROUP BY result.buyerName, result.month;";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<BuyerPlan> getBuyerProfitPlan() {
        return jdbcTemplate.query(SELECT_BUYER_PROFIT_PLAN, BeanPropertyRowMapper.newInstance(BuyerPlan.class));
    }

    @Override
    public List<BuyerPlan> getBuyerRevenuePlan() {
        return jdbcTemplate.query(SELECT_BUYER_REVENUE_PLAN, BeanPropertyRowMapper.newInstance(BuyerPlan.class));
    }
}
