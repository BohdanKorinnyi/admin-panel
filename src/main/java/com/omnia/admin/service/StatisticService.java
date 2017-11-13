package com.omnia.admin.service;

import com.omnia.admin.dto.StatisticFilter;

import java.util.Map;

public interface StatisticService {
    Map<String, Object> getBuyerStatistics(StatisticFilter filter);
}
