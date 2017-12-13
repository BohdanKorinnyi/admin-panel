package com.omnia.admin.dao;

import com.omnia.admin.model.BuyerProfit;

import java.util.List;

public interface DashboardDao {
    List<BuyerProfit> findChartData(Integer buyerId, String from, String to, String filterName);
}
