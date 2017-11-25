package com.omnia.admin.dao;

import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.Source;
import com.omnia.admin.model.SourceStat;

import java.util.List;

public interface SourceStatisticDao {
    List<SourceStat> getSourceStat(StatisticFilter filter);

    List<Source> getStatistics(StatisticFilter filter);

    List<Source> getDailyStatistics(StatisticFilter filter);

    Float getProfitByBuyerId(int buyerId);
}
