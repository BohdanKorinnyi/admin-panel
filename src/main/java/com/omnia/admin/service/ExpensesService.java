package com.omnia.admin.service;

import com.omnia.admin.grid.Page;
import com.omnia.admin.model.Expenses;

import java.util.List;

public interface ExpensesService {
    List<Expenses> getExpenses(Page page, List<Integer> buyerIds, List<Integer> expensesTypes, String from, String to);

    void updateExpenses(Expenses expenses);
}
