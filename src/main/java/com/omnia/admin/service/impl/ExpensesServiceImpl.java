package com.omnia.admin.service.impl;

import com.omnia.admin.dao.ExpensesDao;
import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.Expenses;
import com.omnia.admin.service.ExpensesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public final class ExpensesServiceImpl implements ExpensesService {
    private final ExpensesDao expensesDao;

    @Override
    public List<Expenses> getExpenses(StatisticFilter filter) {
        return expensesDao.getExpenses(filter);
    }
}
