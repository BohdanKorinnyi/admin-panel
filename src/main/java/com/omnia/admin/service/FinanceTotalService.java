package com.omnia.admin.service;

import com.omnia.admin.model.FinanceTotal;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface FinanceTotalService {
    Map<String, List<FinanceTotal>> getFinanceTotal(String from, String to);
}
