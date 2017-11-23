package com.omnia.admin.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.omnia.admin.dao.SourceStatisticDao;
import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.Source;
import com.omnia.admin.model.statistic.SourcesResult;
import com.omnia.admin.service.BuyerService;
import com.omnia.admin.service.SourceStatsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public final class SourceStatsServiceImpl implements SourceStatsService {
    public static final Map<Integer, List<Source>> EMPTY_STATS_MAP = ImmutableMap.of();
    private final BuyerService buyerService;
    private final SourceStatisticDao sourceStatisticDao;

    @Override
    public Map<Integer, SourcesResult> getStatistics(StatisticFilter filter) {
        List<Source> sources = sourceStatisticDao.getStatistics(filter);
        return groupStats(groupByBuyer(sources));
    }

    @Override
    public Map<Integer, SourcesResult> getDailyStatistics(StatisticFilter filter) {
        List<Source> dailySources = sourceStatisticDao.getDailyStatistics(filter);
        return groupStats(groupByBuyer(dailySources));
    }

    @Override
    public Map<Integer, SourcesResult> getDailyAndGeneralStatistics(StatisticFilter filter) {
        Map<Integer, List<Source>> stats = new HashMap<>();
        List<Source> sources = sourceStatisticDao.getStatistics(filter);
        if (isFilterIncludeToday(filter.getTo())) {
            Map<Integer, List<Source>> dailyStats = groupByBuyer(sourceStatisticDao.getDailyStatistics(filter));
            stats = updateAllStats(stats, dailyStats);
        }
        Map<Integer, List<Source>> allStats = groupByBuyer(sources);
        return groupStats(updateAllStats(stats, allStats));
    }

    @Override
    public List<Source> getSources(StatisticFilter filter) {
        List<Source> result = Lists.newArrayList();
        if (isFilterIncludeToday(filter.getTo())) {
            result.addAll(sourceStatisticDao.getDailyStatistics(filter));
        }
        result.addAll(sourceStatisticDao.getStatistics(filter));
        return result;
    }

    @Override
    public Float getProfitByBuyerId(int buyerId) {
        return sourceStatisticDao.getProfitByBuyerId(buyerId);
    }

    private Map<Integer, SourcesResult> groupStats(Map<Integer, List<Source>> stats) {
        Map<Integer, SourcesResult> buyerStatistic = new HashMap<>();
        for (Map.Entry<Integer, List<Source>> entry : stats.entrySet()) {
            SourcesResult buyerCost = createBuyerStatistic(entry.getKey(), entry.getValue());
            buyerCost.setName(buyerService.getBuyerById(entry.getKey()));
            buyerStatistic.put(entry.getKey(), buyerCost);
        }
        return buyerStatistic;
    }
}
