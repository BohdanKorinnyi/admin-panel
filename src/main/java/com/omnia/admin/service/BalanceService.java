package com.omnia.admin.service;

import com.omnia.admin.model.Balance;

import java.util.List;

public interface BalanceService {
    List<Balance> getMonthBalance(int year, String month, List<Long> advertiserIds);
}
