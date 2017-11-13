package com.omnia.admin.dao;

import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.SourceStatistic;

import java.util.List;

public interface SourceStatisticDao {
    List<SourceStatistic> getStatistics(StatisticFilter filter);

    List<SourceStatistic> getDailyStatistics(StatisticFilter filter);
}
