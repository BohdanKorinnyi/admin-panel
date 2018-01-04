package com.omnia.admin.service;

import com.omnia.admin.model.Income;

@FunctionalInterface
public interface IncomeService {
    void save(Income income);
}
