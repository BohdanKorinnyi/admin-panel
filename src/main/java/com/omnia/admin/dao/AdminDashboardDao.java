package com.omnia.admin.dao;

import com.omnia.admin.model.BuyerProfit;

import java.util.List;

public interface AdminDashboardDao {
    List<BuyerProfit> findAllBuyersProfit(String from, String to);

    List<BuyerProfit> findChartData(String from, String to, String filterName);

    BuyerProfit findRecentBuyersProfit(boolean today);
}
