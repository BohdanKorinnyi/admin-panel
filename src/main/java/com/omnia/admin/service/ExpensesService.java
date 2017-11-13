package com.omnia.admin.service;

import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.Expenses;

import java.util.List;

public interface ExpensesService {
    List<Expenses> getExpenses(StatisticFilter filter);
}
