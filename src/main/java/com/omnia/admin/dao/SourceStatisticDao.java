package com.omnia.admin.dao;

import com.omnia.admin.dto.SourceStatFilter;
import com.omnia.admin.model.SourceStatistic;

import java.util.List;

public interface SourceStatisticDao {
    List<SourceStatistic> getStatistics(SourceStatFilter filter);

    List<SourceStatistic> getDailyStatistics(SourceStatFilter filter);
}
