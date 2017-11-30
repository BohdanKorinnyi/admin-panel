package com.omnia.admin.service.impl;

import com.omnia.admin.dao.ExpensesDao;
import com.omnia.admin.grid.Page;
import com.omnia.admin.model.Expenses;
import com.omnia.admin.service.ExpensesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public final class ExpensesServiceImpl implements ExpensesService {
    private final ExpensesDao expensesDao;

    @Override
    public Map<String, Object> getExpenses(Page page, List<Integer> buyerIds, List<Integer> expensesType, String from, String to) {
        return expensesDao.getExpenses(page, buyerIds, expensesType, from, to);
    }

    @Override
    public void save(List<Expenses> expenses) {
        expensesDao.save(expenses);
    }

    @Override
    public void updateExpenses(List<Expenses> expenses) {
        expensesDao.update(expenses);
    }

    @Override
    public void delete(List<Integer> ids) {
        expensesDao.delete(ids);
    }
}
