package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.ExpensesDao;
import com.omnia.admin.grid.Page;
import com.omnia.admin.model.Expenses;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.omnia.admin.grid.filter.FilterConstant.EMPTY;
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

@Log4j
@Repository
@AllArgsConstructor
public class ExpensesDaoImpl implements ExpensesDao {
    private static final String INSERT_EXPENSES = "INSERT INTO expenses (" +
            "buyer_id, type_id, sum, date, description, date_create, date_change) " +
            "VALUES (?,?,?,?,?,now(),now());";
    private static final String UPDATE_EXPENSES = "UPDATE expenses " +
            "SET buyer_id = ?, date = ?, sum = ?, type_id = ? WHERE id = ?;";
    private static final String DELETE_EXPENSES = "DELETE FROM expenses WHERE id IN (%s)";
    private static final String SELECT_COUNT_EXPENSES = "SELECT" +
            "  COUNT(expenses.id) " +
            "FROM expenses" +
            "  LEFT JOIN expenses_type ON expenses.type_id = expenses_type.id " +
            "WHERE expenses.sum != 0 %s" +
            "ORDER BY date DESC ";
    private static final String SELECT_EXPENSES = "SELECT" +
            "  expenses.*,expenses_type.name " +
            "FROM expenses" +
            "  LEFT JOIN expenses_type ON expenses.type_id = expenses_type.id " +
            "WHERE expenses.sum != 0 %s" +
            "ORDER BY date DESC ";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, Object> getExpenses(Page page, List<Integer> buyerIds, List<Integer> expensesType, String from, String to) {
        Map<String, Object> result = new HashMap<>();
        String whereClause = createWhereClause(buyerIds, expensesType, from, to);
        result.put("data", jdbcTemplate.query(String.format(SELECT_EXPENSES, whereClause) + page.limit(), BeanPropertyRowMapper.newInstance(Expenses.class)));
        result.put("size", jdbcTemplate.queryForObject(String.format(SELECT_COUNT_EXPENSES, whereClause), Integer.class));
        return result;
    }

    @Override
    public void save(List<Expenses> expenses) {
        jdbcTemplate.batchUpdate(INSERT_EXPENSES, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, expenses.get(i).getBuyerId());
                preparedStatement.setInt(2, expenses.get(i).getTypeId());
                preparedStatement.setFloat(3, expenses.get(i).getSum());
                preparedStatement.setDate(4, Date.valueOf(expenses.get(i).getDate()));
                preparedStatement.setString(5, expenses.get(i).getDescription());
            }

            @Override
            public int getBatchSize() {
                return expenses.size();
            }
        });
    }

    @Override
    public void update(List<Expenses> expenses) {
        jdbcTemplate.batchUpdate(UPDATE_EXPENSES, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, expenses.get(i).getBuyerId());
                preparedStatement.setDate(2, Date.valueOf(expenses.get(i).getDate()));
                preparedStatement.setFloat(3, expenses.get(i).getSum());
                preparedStatement.setInt(4, expenses.get(i).getTypeId());
                preparedStatement.setLong(5, expenses.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return expenses.size();
            }
        });
    }

    @Override
    public void delete(List<Integer> ids) {
        jdbcTemplate.execute(String.format(DELETE_EXPENSES, StringUtils.collectionToCommaDelimitedString(ids)));
    }

    private String createWhereClause(List<Integer> buyerIds, List<Integer> expensesType, String from, String to) {
        String where = EMPTY;
        if (!CollectionUtils.isEmpty(buyerIds)) {
            where = " AND expenses.buyer_id IN (" + collectionToCommaDelimitedString(buyerIds) + ") ";
        }
        if (!CollectionUtils.isEmpty(expensesType)) {
            where = " AND expenses_type.id IN (" + collectionToCommaDelimitedString(expensesType) + ") ";
        }
        if (!StringUtils.isEmpty(from) && !StringUtils.isEmpty(to)) {
            where += " AND date BETWEEN '" + from + "' AND '" + to + "' ";
        }
        return where;
    }
}
