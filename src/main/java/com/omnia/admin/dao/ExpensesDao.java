package com.omnia.admin.dao;

import com.omnia.admin.grid.Page;
import com.omnia.admin.model.Expenses;

import java.util.List;
import java.util.Map;

public interface ExpensesDao {
    Map<String, Object> getExpenses(Page page, List<Integer> buyerIds, List<Integer> expensesType, String from, String to);

    void update(List<Expenses> expenses);

    void delete(List<Integer> ids);
}
