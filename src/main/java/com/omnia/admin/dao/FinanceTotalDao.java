package com.omnia.admin.dao;

import com.omnia.admin.model.FinanceTotal;

import java.util.List;

@FunctionalInterface
public interface FinanceTotalDao {
    List<FinanceTotal> findFinanceTotalByTimeRange(String from, String to);
}
