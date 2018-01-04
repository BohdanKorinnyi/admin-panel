package com.omnia.admin.dao;

import com.omnia.admin.model.Income;

@FunctionalInterface
public interface IncomeDao {
    void save(Income income);
}
