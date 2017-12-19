package com.omnia.admin.dao;

import com.omnia.admin.model.BuyerCosts;

import java.util.List;

public interface SpentDao {
    Float calculateBuyerCurrentMonthSpent(Integer buyerId);

    Float calculateBuyerTodaySpent(Integer buyerId);

    Float calculateBuyerYesterdaySpent(Integer buyerId);

    List<BuyerCosts> createSpentByBuyerReport(String from, String to);
}
