package com.omnia.admin.dao.impl;

import com.google.common.collect.ImmutableSet;
import com.omnia.admin.dao.PayrollDao;
import com.omnia.admin.grid.Page;
import com.omnia.admin.model.ColumnOrder;
import com.omnia.admin.model.Payroll;
import com.omnia.admin.model.PayrollType;
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
    private static final String SELECT_PAYROLLS = "SELECT * FROM payroll_new ";
    private static final String SELECT_COUNT_PAYROLLS = "SELECT COUNT(*) FROM payroll_new";
    private static final String UPDATE_PAYROLL = "UPDATE payroll_new SET staff_id = ?, date = ?, description = ?, type_id = ?, sum = ?, currency_id = ? WHERE id = ?;";
    private static final String INSERT_PAYROLL = "INSERT INTO payroll_new (staff_id, date, description, type_id, sum, currency_id, periond) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String DELETE_PAYROLL = "DELETE FROM payroll_new WHERE id = ?";
    private static final String SELECT_PAYROLL_DESCRIPTION = "SELECT name FROM payroll_description";
    private static final String SELECT_PAYROLL_TYPES = "SELECT * FROM payroll_type";

    private static final String SELECT_PAYROLL_BY_STAFF = "SELECT payroll_new.* " +
            "FROM payroll_new " +
            "LEFT JOIN buyer_payments ON payroll_new.id = buyer_payments.payroll_id " +
            "WHERE payroll_new.staff_id =? AND buyer_payments.id IS NULL ;";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Payroll> getPayrollsByBuyerAndYear(int buyerId, int year) {
        return jdbcTemplate.query("SELECT " +
                "  payroll_new.sum             AS 'value', " +
                "  currency.code           AS 'currency', " +
                "  monthname(payroll_new.date) AS 'month' " +
                "FROM payroll_new " +
                "  INNER JOIN payroll_type ON payroll_type.id = payroll_new.type_id " +
                "  LEFT JOIN currency ON payroll_new.currency_id = currency.id " +
                "  LEFT JOIN staff ON staff.id = payroll_new.staff_id " +
                "  LEFT JOIN buyers ON staff.buyer_id = buyers.id " +
                "WHERE payroll_type.type = 'bonus' AND buyers.id = ? AND year(payroll_new.date) = ? " +
                "GROUP BY month(payroll_new.date) " +
                "ORDER BY month(payroll_new.date)", BeanPropertyRowMapper.newInstance(Payroll.class), buyerId, year);
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
        return jdbcTemplate.query(SELECT_PAYROLLS + " LEFT JOIN staff ON staff.id = payroll_new.staff_id LEFT JOIN buyers ON staff.buyer_id = buyers.id WHERE buyers.id = ?", new BeanPropertyRowMapper<>(Payroll.class), buyerId);
    }

    @Override
    @Transactional
    public void update(Payroll payroll) {
        jdbcTemplate.update(UPDATE_PAYROLL, payroll.getStaffId(), payroll.getDate(), payroll.getDescription(),
                payroll.getTypeId(), payroll.getSum(), payroll.getCurrencyId(), payroll.getId());
    }

    @Override
    @Transactional
    public void save(List<Payroll> payrolls) {
        jdbcTemplate.batchUpdate(INSERT_PAYROLL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, payrolls.get(i).getStaffId());
                ps.setDate(2, payrolls.get(i).getDate());
                ps.setString(3, payrolls.get(i).getDescription());
                ps.setInt(4, payrolls.get(i).getTypeId());
                ps.setFloat(5, payrolls.get(i).getSum());
                ps.setInt(6, payrolls.get(i).getCurrencyId());
                ps.setDate(7, payrolls.get(i).getPeriond());
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

    @Override
    public List<PayrollType> getTypes() {
        return jdbcTemplate.query(SELECT_PAYROLL_TYPES, BeanPropertyRowMapper.newInstance(PayrollType.class));
    }

    @Override
    public List<Payroll> getPayrollsByStaffId(int staffId) {
        return jdbcTemplate.query(SELECT_PAYROLL_BY_STAFF, BeanPropertyRowMapper.newInstance(Payroll.class), staffId);
    }

    private boolean isValidSortDetails(ColumnOrder columnOrder) {
        return nonNull(columnOrder) && columnOrder.isValid() && SORTED_PAYROLL_COLUMNS.contains(columnOrder.getColumn());
    }

    private String addBuyerFilter(Integer buyerId) {
        if (nonNull(buyerId)) {
            return " LEFT JOIN staff ON staff.id = payroll_new.staff_id LEFT JOIN buyers ON staff.buyer_id = buyers.id WHERE buyers.id = " + buyerId + " ";
        }
        return EMPTY;
    }
}
