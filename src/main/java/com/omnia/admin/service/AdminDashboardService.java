package com.omnia.admin.service;

public interface AdminDashboardService {
    Object getData(String from, String to);

    Object getChartData(String from, String to, String filterName);
}
