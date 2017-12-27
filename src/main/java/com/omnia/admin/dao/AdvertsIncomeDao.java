package com.omnia.admin.dao;

import com.omnia.admin.model.AdvertiserIncome;

import java.util.List;

public interface AdvertsIncomeDao {
    Float getTotalByPeriod(List<Integer> advertiserIds, String from, String to);

    List<AdvertiserIncome> getIncomeByPeriodGroupedByAdvertiser(List<Integer> advertiserIds, String from, String to);
}
