package com.omnia.admin.service;

import com.omnia.admin.model.ExpensesType;

import java.util.List;

public interface ExpensesTypeService {
    void save(String name);

    void update(ExpensesType expensesType);

    List<ExpensesType> findAll();
}
