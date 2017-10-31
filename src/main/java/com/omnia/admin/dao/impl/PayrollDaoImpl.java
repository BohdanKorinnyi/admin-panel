package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.PayrollDao;
import com.omnia.admin.grid.Page;
import com.omnia.admin.model.Payroll;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@AllArgsConstructor
public class PayrollDaoImpl implements PayrollDao {

    private static final String SELECT_PAYROLLS = "SELECT * FROM payrolls ";
    private static final String SELECT_COUNT_PAYROLLS = "SELECT COUNT(*) FROM payrolls";
    private static final String UPDATE_PAYROLL = "UPDATE payroll SET buyer_id = ?, date = ?, description = ?, type = ?, sum = ?, currency_id = ? WHERE id = ?;";
    private static final String INSERT_PAYROLL = "INSERT INTO payroll (buyer_id, date, description, type, sum, currency_id) VALUES (?, ?, ?, ?, ?, ?);";
    private static final String DELETE_PAYTOLL = "DELETE FROM payroll WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Integer countAll() {
        return jdbcTemplate.queryForObject(SELECT_COUNT_PAYROLLS, Integer.class);
    }

    @Override
    public List<Payroll> findPayrolls(Page page) {
        return jdbcTemplate.query(SELECT_PAYROLLS + page.limit(), new BeanPropertyRowMapper<>(Payroll.class));
    }

    @Override
    public void update(List<Payroll> payrolls) {
        jdbcTemplate.batchUpdate(UPDATE_PAYROLL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, payrolls.get(i).getBuyerId());
                ps.setDate(2, payrolls.get(i).getDate());
                ps.setString(3, payrolls.get(i).getDescription());
                ps.setInt(4, payrolls.get(i).getType());
                ps.setFloat(5, payrolls.get(i).getSum());
                ps.setInt(6, payrolls.get(i).getCurrencyId());
                ps.setLong(7, payrolls.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return payrolls.size();
            }
        });
    }

    @Override
    public void save(List<Payroll> payrolls) {
        jdbcTemplate.batchUpdate(INSERT_PAYROLL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, payrolls.get(i).getBuyerId());
                ps.setDate(2, payrolls.get(i).getDate());
                ps.setString(3, payrolls.get(i).getDescription());
                ps.setInt(4, payrolls.get(i).getType());
                ps.setFloat(5, payrolls.get(i).getSum());
                ps.setInt(6, payrolls.get(i).getCurrencyId());
            }

            @Override
            public int getBatchSize() {
                return payrolls.size();
            }
        });
    }

    @Override
    public void delete(List<Long> ids) {
        jdbcTemplate.batchUpdate(DELETE_PAYTOLL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, ids.get(i));
            }

            @Override
            public int getBatchSize() {
                return ids.size();
            }
        });
    }
}
