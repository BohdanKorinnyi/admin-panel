package com.omnia.admin.dao;

import com.omnia.admin.dto.StatisticFilterDto;
import com.omnia.admin.model.Statistic;

import java.util.List;

public interface StatisticDao {
    List<Statistic> getStatistics(StatisticFilterDto filter);
}
