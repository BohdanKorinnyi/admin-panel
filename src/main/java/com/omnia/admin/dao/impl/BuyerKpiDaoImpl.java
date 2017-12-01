package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.BuyerKpiDao;
import com.omnia.admin.model.BuyerKpi;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Log4j
@Repository
@AllArgsConstructor
public class BuyerKpiDaoImpl implements BuyerKpiDao {
    private static final String SELECT_BUYER_KPI_BY_BUYER_ID =
            "SELECT " +
                    "  buyers_kpi.*, " +
                    "  catalog_kpi.name AS 'kpi_catalog_name' " +
                    "FROM buyers_kpi " +
                    "  LEFT JOIN catalog_kpi ON buyers_kpi.kpi_name = catalog_kpi.id " +
                    "WHERE buyer_id = ?;";
    private static final String INSERT_BUYER_KPI = "INSERT INTO buyers_kpi (buyer_id, date, kpi_name, kpi_value) VALUES (?, ?, ?, ?);";
    private static final String SELECT_BUYER_PLAN = "SELECT kpi_value " +
            "FROM buyers_kpi " +
            "  LEFT JOIN catalog_kpi ON buyers_kpi.kpi_name = catalog_kpi.id " +
            "WHERE buyer_id = ? AND month(date) = month(now()) AND catalog_kpi.name = ?;";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<BuyerKpi> findByBuyerId(int buyerId) {
        return jdbcTemplate.query(SELECT_BUYER_KPI_BY_BUYER_ID, BeanPropertyRowMapper.newInstance(BuyerKpi.class), buyerId);
    }

    @Override
    public void save(List<BuyerKpi> kpis, int buyerId) {
        jdbcTemplate.batchUpdate(INSERT_BUYER_KPI, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, buyerId);
                ps.setDate(2, Date.valueOf(kpis.get(i).getDate()));
                ps.setInt(3, kpis.get(i).getKpiName());
                ps.setFloat(4, kpis.get(i).getKpiValue());
            }

            @Override
            public int getBatchSize() {
                return kpis.size();
            }
        });
    }

    @Override
    public Float getBuyerRevenuePlan(Integer buyerId) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BUYER_PLAN, Float.class, buyerId, "Revenue");
        } catch (EmptyResultDataAccessException e) {
            log.error("Not found any data about buyer revenue plan by buyerId=" + buyerId);
        } catch (Exception e) {
            log.error("Error occurred during getting revenue by buyerId=" + buyerId, e);
        }
        return 0F;
    }

    @Override
    public Float getBuyerProfitPlan(Integer buyerId) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BUYER_PLAN, Float.class, buyerId, "Profit");
        } catch (EmptyResultDataAccessException e) {
            log.error("Not found any data about buyer profit plan by buyerId=" + buyerId);
        } catch (Exception e) {
            log.error("Error occurred during getting profit by buyerId=" + buyerId, e);
        }
        return 0F;
    }
}
