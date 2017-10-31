package com.omnia.admin.dao.impl;

import com.google.common.collect.ImmutableSet;
import com.omnia.admin.dao.PayrollDao;
import com.omnia.admin.grid.Page;
import com.omnia.admin.model.ColumnOrder;
import com.omnia.admin.model.Payroll;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static com.omnia.admin.grid.filter.FilterConstant.EMPTY;
import static java.util.Objects.nonNull;

@Repository
@AllArgsConstructor
public class PayrollDaoImpl implements PayrollDao {

    private static final Set<String> SORTED_PAYROLL_COLUMNS = ImmutableSet.of("date", "buyer_id");
    private static final String ORDER_BY = "ORDER BY %s %s";
    private static final String SELECT_PAYROLLS = "SELECT * FROM payroll ";
    private static final String SELECT_COUNT_PAYROLLS = "SELECT COUNT(*) FROM payroll";
    private static final String UPDATE_PAYROLL = "UPDATE payroll SET buyer_id = ?, date = ?, description = ?, type = ?, sum = ?, currency_id = ? WHERE id = ?;";
    private static final String INSERT_PAYROLL = "INSERT INTO payroll (buyer_id, date, description, type, sum, currency_id) VALUES (?, ?, ?, ?, ?, ?);";
    private static final String DELETE_PAYROLL = "DELETE FROM payroll WHERE id = ?";
    private static final String SELECT_PAYROLL_DESCRIPTION = "SELECT name FROM payroll_description";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Integer countAll() {
        return jdbcTemplate.queryForObject(SELECT_COUNT_PAYROLLS, Integer.class);
    }

    @Override
    public List<Payroll> findPayrolls(Page page) {
        String where = EMPTY;
        ColumnOrder columnOrder = page.getColumnOrder();
        if (isValidSortDetails(page.getColumnOrder())) {
            where = String.format(ORDER_BY, columnOrder.getColumn(), columnOrder.getOrder());
        }
        return jdbcTemplate.query(SELECT_PAYROLLS + where + page.limit(), new BeanPropertyRowMapper<>(Payroll.class));
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
        jdbcTemplate.batchUpdate(DELETE_PAYROLL, new BatchPreparedStatementSetter() {
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

    @Override
    public List<String> getPayrollDescription() {
        return jdbcTemplate.queryForList(SELECT_PAYROLL_DESCRIPTION, String.class);
    }

    private boolean isValidSortDetails(ColumnOrder columnOrder) {
        if (nonNull(columnOrder) && columnOrder.isValid()) {
            return SELECT_PAYROLL_DESCRIPTION.contains(columnOrder.getColumn());
        }
        return false;
    }
}