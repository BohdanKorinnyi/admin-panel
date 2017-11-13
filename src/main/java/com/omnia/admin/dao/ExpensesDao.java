package com.omnia.admin.dao;

import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.Expenses;

import java.util.List;

public interface ExpensesDao {
    List<Expenses> getExpenses(StatisticFilter filter);
}
