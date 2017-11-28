package com.omnia.admin.dao;

import com.omnia.admin.grid.Page;
import com.omnia.admin.model.Expenses;

import java.util.List;

public interface ExpensesDao {
    List<Expenses> getExpenses(Page page, List<Integer> buyerIds, String from, String to);

    void update(Expenses expenses);
}
