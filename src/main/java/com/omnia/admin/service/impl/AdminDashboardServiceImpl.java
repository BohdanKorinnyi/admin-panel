package com.omnia.admin.service.impl;

import com.omnia.admin.dao.AdminDashboardDao;
import com.omnia.admin.service.AdminDashboardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@AllArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {
    private AdminDashboardDao adminDashboardDao;

    @Override
    public Object getData(String from, String to) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("data", adminDashboardDao.findAllBuyersProfit(from, to));
        result.put("today", adminDashboardDao.findRecentBuyersProfit(true));
        result.put("yesterday", adminDashboardDao.findRecentBuyersProfit(false));
        return result;
    }

    @Override
    public Object getChartData(String from, String to, String filterName) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("data", adminDashboardDao.findChartData(from, to, filterName));
        return result;
    }
}
