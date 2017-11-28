package com.omnia.admin.dao;

import com.omnia.admin.model.ExpensesType;

import java.util.List;

public interface ExpensesTypeDao {
    void save(String name);

    void update(ExpensesType expensesType);

    List<ExpensesType> findAll();
}
