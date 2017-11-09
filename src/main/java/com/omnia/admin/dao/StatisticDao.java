package com.omnia.admin.dao;

import com.omnia.admin.dto.StatFilter;
import com.omnia.admin.model.Statistic;

import java.util.List;

public interface StatisticDao {
    List<Statistic> getStatistics(StatFilter filter);

    List<Statistic> getDailyStatistics(StatFilter filter);
}
