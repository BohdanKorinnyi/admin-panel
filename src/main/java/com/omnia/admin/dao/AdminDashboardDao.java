package com.omnia.admin.dao;

import com.omnia.admin.model.BuyerProfit;

import java.util.List;

public interface AdminDashboardDao {
    List<BuyerProfit> findAllBuyersProfit(String from, String to);

    BuyerProfit findRecentBuyersProfit(boolean today);
}
