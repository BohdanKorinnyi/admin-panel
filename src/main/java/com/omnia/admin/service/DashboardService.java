package com.omnia.admin.service;

import com.omnia.admin.model.BuyerProfit;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface DashboardService {
    Map<String, Object> getDashboardData(Integer buyerId) throws ExecutionException, InterruptedException;

    Map<String, List<BuyerProfit>> getChartData(int buyerId, String from, String to, String filter);
}
