package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.ExpensesDao;
import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.Expenses;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.StringJoiner;

@Repository
@AllArgsConstructor
public class ExpensesDaoImpl implements ExpensesDao {
    private static final String SELECT_EXPENSES = "SELECT buyers.name as buyer,expenses.date,expenses.sum,expenses_type.name " +
            "FROM expenses" +
            "  LEFT JOIN buyers ON expenses.buyer_id = buyers.id" +
            "  LEFT JOIN expenses_type ON expenses.type_id = expenses_type.id %s" +
            "ORDER BY date DESC";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Expenses> getExpenses(StatisticFilter filter) {
        String where = "";
        if (!CollectionUtils.isEmpty(filter.getBuyers())) {
            StringJoiner joiner = new StringJoiner("','", "'", "'");
            filter.getBuyers().forEach(joiner::add);
            where += " buyers.name IN (" + joiner.toString() + ") ";
        }
        if (!StringUtils.isEmpty(filter.getFrom()) && !StringUtils.isEmpty(filter.getTo())) {
            if (!StringUtils.isEmpty(where)) {
                where += "AND";
            }
            where += " date BETWEEN '" + filter.getFrom() + "' AND '" + filter.getTo() + "' ";
        }

        if (!StringUtils.isEmpty(where)) {
            where = "WHERE " + where;
        }

        return jdbcTemplate.query(String.format(SELECT_EXPENSES, where), BeanPropertyRowMapper.newInstance(Expenses.class));
    }
}
