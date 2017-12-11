package com.omnia.admin.dao;

public interface SpentDao {
    Float calculateBuyerAllTimeSpent(Integer buyerId);

    Float calculateBuyerCurrentMonthSpent(Integer buyerId);

    Float calculateBuyerCustomRangeSpent(Integer buyerId);
}
