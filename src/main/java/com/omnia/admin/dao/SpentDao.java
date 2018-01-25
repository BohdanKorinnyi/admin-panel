package com.omnia.admin.dao;

import com.omnia.admin.model.BuyerCosts;
import com.omnia.admin.model.Spent;

import java.util.List;

public interface SpentDao {
    List<Spent> getSpentByBuyerAndYear(int buyer, int year);

    Float calculateBuyerCurrentMonthSpent(Integer buyerId);

    Float calculateBuyerTodaySpent(Integer buyerId);

    Float calculateBuyerYesterdaySpent(Integer buyerId);

    List<BuyerCosts> getSpentReport(List<Integer> buyerIds, List<String> sources, String from, String to);
}
