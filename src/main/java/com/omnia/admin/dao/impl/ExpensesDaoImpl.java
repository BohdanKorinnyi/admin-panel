package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.ExpensesDao;
import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.Expenses;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.omnia.admin.grid.filter.FilterConstant.EMPTY;

@Log4j
@Repository
@AllArgsConstructor
public class ExpensesDaoImpl implements ExpensesDao {
    private static final String SELECT_EXPENSES = "SELECT" +
            "  expenses.buyer_id AS buyerId," +
            "  expenses.date," +
            "  expenses.sum," +
            "  expenses_type.name" +
            "FROM expenses" +
            "  LEFT JOIN expenses_type ON expenses.type_id = expenses_type.id " +
            "WHERE expenses.sum != 0 %s" +
            "ORDER BY date DESC";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Expenses> getExpenses(StatisticFilter filter) {
        return jdbcTemplate.query(createSql(filter), BeanPropertyRowMapper.newInstance(Expenses.class));
    }

    private String createSql(StatisticFilter filter) {
        String where = EMPTY;
        if (!CollectionUtils.isEmpty(filter.getBuyers())) {
            where = " AND expenses.buyer_id IN (" + StringUtils.collectionToCommaDelimitedString(filter.getBuyers()) + ") ";
        }
        if (!StringUtils.isEmpty(filter.getFrom()) && !StringUtils.isEmpty(filter.getTo())) {
            where += " AND date BETWEEN '" + filter.getFrom() + "' AND '" + filter.getTo() + "' ";
        }
        return String.format(SELECT_EXPENSES, where);
    }
}
