package com.omnia.admin.service;

import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.statistic.Stats;

import java.util.List;

public interface StatisticService {
    List<Stats> getBuyerStatistics(StatisticFilter filter);
}
