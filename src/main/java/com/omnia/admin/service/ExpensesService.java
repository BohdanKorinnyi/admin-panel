package com.omnia.admin.service;

import com.omnia.admin.grid.Page;
import com.omnia.admin.model.Expenses;

import java.util.List;
import java.util.Map;

public interface ExpensesService {
    Map<String, Object> getExpenses(Page page, List<Integer> buyerIds, List<Integer> expensesTypes, String from, String to);

    void updateExpenses(List<Expenses> expenses);

    void delete(List<Integer> ids);
}
