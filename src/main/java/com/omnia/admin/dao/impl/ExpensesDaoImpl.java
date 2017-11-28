package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.ExpensesDao;
import com.omnia.admin.grid.Page;
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
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;
import static org.springframework.util.StringUtils.replace;

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
            "ORDER BY date DESC ";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Expenses> getExpenses(Page page, List<Integer> buyerIds, String from, String to) {
        String sql = createSql(buyerIds, from, to);
        return jdbcTemplate.query(sql + page.limit(), BeanPropertyRowMapper.newInstance(Expenses.class));
    }

    @Override
    public void update(Expenses expenses) {

    }

    private String createSql(List<Integer> buyerIds, String from, String to) {
        String where = EMPTY;
        if (!CollectionUtils.isEmpty(buyerIds)) {
            where = " AND expenses.buyer_id IN (" + collectionToCommaDelimitedString(buyerIds) + ") ";
        }
        if (!StringUtils.isEmpty(from) && !StringUtils.isEmpty(to)) {
            where += " AND date BETWEEN '" + from + "' AND '" + to + "' ";
        }
        return String.format(SELECT_EXPENSES, where);
    }
}
