package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.ExpensesTypeDao;
import com.omnia.admin.model.ExpensesType;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Log4j
@Repository
@AllArgsConstructor
public class ExpensesTypeDaoImpl implements ExpensesTypeDao {
    private static final String SELECT_EXPENSES_TYPES = "SELECT * FROM expenses_type";
    private static final String INSERT_EXPENSES_TYPE = "INSERT INTO expenses_type (name) VALUES (?);";
    private static final String UPDATE_EXPENSES_TYPE = "UPDATE expenses_type SET name = ? WHERE id = ?;";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(String name) {
        try {
            jdbcTemplate.update(INSERT_EXPENSES_TYPE, name);
        } catch (Exception e) {
            log.error("Error occurred during saving expenses type", e);
        }
    }

    @Override
    public void update(ExpensesType expensesType) {
        jdbcTemplate.update(UPDATE_EXPENSES_TYPE, expensesType.getName(), expensesType.getId());
    }

    @Override
    public List<ExpensesType> findAll() {
        return jdbcTemplate.query(SELECT_EXPENSES_TYPES, BeanPropertyRowMapper.newInstance(ExpensesType.class));
    }
}
