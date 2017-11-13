package com.omnia.admin.service.impl;

import com.google.common.collect.ImmutableMap;
import com.omnia.admin.dao.SourceStatisticDao;
import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.BuyerStatistic;
import com.omnia.admin.model.SourceStatistic;
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
    public static final Map<Integer, List<SourceStatistic>> EMPTY_STATS_MAP = ImmutableMap.of();
    private final BuyerService buyerService;
    private final SourceStatisticDao sourceStatisticDao;

    @Override
    public Map<Integer, BuyerStatistic> getStatistics(StatisticFilter filter) {
        List<SourceStatistic> sourceStatistics = sourceStatisticDao.getStatistics(filter);
        return groupStats(groupByBuyer(sourceStatistics));
    }

    @Override
    public Map<Integer, BuyerStatistic> getDailyStatistics(StatisticFilter filter) {
        List<SourceStatistic> dailySourceStatistics = sourceStatisticDao.getDailyStatistics(filter);
        return groupStats(groupByBuyer(dailySourceStatistics));
    }

    @Override
    public Map<Integer, BuyerStatistic> getAllStatistics(StatisticFilter filter) {
        Map<Integer, List<SourceStatistic>> stats = new HashMap<>();
        List<SourceStatistic> sourceStatistics = sourceStatisticDao.getStatistics(filter);
        if (isFilterIncludeToday(filter.getTo())) {
            Map<Integer, List<SourceStatistic>> dailyStats = groupByBuyer(sourceStatisticDao.getDailyStatistics(filter));
            stats = updateAllStats(stats, dailyStats);
        }
        Map<Integer, List<SourceStatistic>> allStats = groupByBuyer(sourceStatistics);
        return groupStats(updateAllStats(stats, allStats));
    }

    private Map<Integer, BuyerStatistic> groupStats(Map<Integer, List<SourceStatistic>> stats) {
        Map<Integer, BuyerStatistic> buyerStatistic = new HashMap<>();

        for (Map.Entry<Integer, List<SourceStatistic>> entry : stats.entrySet()) {
            BuyerStatistic buyerCost = createBuyerStatistic(entry.getKey(), entry.getValue());
            buyerCost.setBuyerName(buyerService.getBuyerById(entry.getKey()));
            buyerStatistic.put(entry.getKey(), buyerCost);
        }
        return buyerStatistic;
    }
}
