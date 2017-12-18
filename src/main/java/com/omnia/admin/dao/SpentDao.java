package com.omnia.admin.dao;

public interface SpentDao {
    Float calculateBuyerCurrentMonthSpent(Integer buyerId);

    Float calculateBuyerTodaySpent(Integer buyerId);

    Float calculateBuyerYesterdaySpent(Integer buyerId);
}
