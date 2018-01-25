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
import org.springframework.transaction.annotation.Transactional;

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
    private static final String ORDER_BY = " ORDER BY %s %s";
    private static final String SELECT_PAYROLLS = "SELECT * FROM payroll ";
    private static final String SELECT_COUNT_PAYROLLS = "SELECT COUNT(*) FROM payroll";
    private static final String UPDATE_PAYROLL = "UPDATE payroll SET buyer_id = ?, date = ?, description = ?, type = ?, sum = ?, currency_id = ? WHERE id = ?;";
    private static final String INSERT_PAYROLL = "INSERT INTO payroll (buyer_id, date, description, type, sum, currency_id) VALUES (?, ?, ?, ?, ?, ?);";
    private static final String DELETE_PAYROLL = "DELETE FROM payroll WHERE id = ?";
    private static final String SELECT_PAYROLL_DESCRIPTION = "SELECT name FROM payroll_description";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Payroll> getPayrollsByBuyerAndYear(int buyerId, int year) {
        return jdbcTemplate.query("SELECT " +
                "  payroll.sum             AS 'sum', " +
                "  currency.code           AS 'currency', " +
                "  monthname(payroll.date) AS 'month' " +
                "FROM payroll " +
                "  INNER JOIN payroll_type ON payroll_type.id = payroll.type " +
                "  LEFT JOIN currency ON payroll.currency_id = currency.id " +
                "WHERE payroll_type.type = 'bonus' AND payroll.buyer_id = ? AND year(payroll.date) = ? " +
                "GROUP BY month(payroll.date) " +
                "ORDER BY month(payroll.date)", BeanPropertyRowMapper.newInstance(Payroll.class), buyerId, year);
    }

    @Override
    public Integer countAll(Integer buyerId) {
        return jdbcTemplate.queryForObject(SELECT_COUNT_PAYROLLS + addBuyerFilter(buyerId), Integer.class);
    }

    @Override
    public List<Payroll> findPayrolls(Page page, Integer buyerId) {
        String where = EMPTY;
        ColumnOrder columnOrder = page.getColumnOrder();
        if (isValidSortDetails(page.getColumnOrder())) {
            where = String.format(ORDER_BY, columnOrder.getColumn(), columnOrder.getOrder());
        }
        return jdbcTemplate.query(SELECT_PAYROLLS + addBuyerFilter(buyerId) + where + page.limit(), new BeanPropertyRowMapper<>(Payroll.class));
    }

    @Override
    public List<Payroll> findPayrollsByBuyerId(Integer buyerId) {
        return jdbcTemplate.query(SELECT_PAYROLLS + " WHERE buyer_id = ?", new BeanPropertyRowMapper<>(Payroll.class), buyerId);
    }

    @Override
    @Transactional
    public void update(Payroll payroll) {
        jdbcTemplate.update(UPDATE_PAYROLL, payroll.getBuyerId(), payroll.getDate(), payroll.getDescription(),
                payroll.getType(), payroll.getSum(), payroll.getCurrencyId(), payroll.getId());
    }

    @Override
    @Transactional
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
    @Transactional
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
        return nonNull(columnOrder) && columnOrder.isValid() && SORTED_PAYROLL_COLUMNS.contains(columnOrder.getColumn());
    }

    private String addBuyerFilter(Integer buyerId) {
        if (nonNull(buyerId)) {
            return " WHERE buyer_id = " + buyerId + " ";
        }
        return EMPTY;
    }
}
