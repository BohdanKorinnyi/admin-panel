package com.omnia.admin.dao;

import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.Source;

import java.util.List;

public interface SourceStatisticDao {
    List<Source> getStatistics(StatisticFilter filter);

    List<Source> getDailyStatistics(StatisticFilter filter);

    Float getProfitByBuyerId(int buyerId);
}
