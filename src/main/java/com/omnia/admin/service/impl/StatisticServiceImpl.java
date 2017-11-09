package com.omnia.admin.service.impl;

import com.google.common.collect.ImmutableMap;
import com.omnia.admin.dao.StatisticDao;
import com.omnia.admin.dto.StatisticFilterDto;
import com.omnia.admin.model.BuyerStatistic;
import com.omnia.admin.model.Statistic;
import com.omnia.admin.service.BuyerService;
import com.omnia.admin.service.StatisticService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    public static final Map<Integer, List<Statistic>> EMPTY_STATS_MAP = ImmutableMap.of();

    private final BuyerService buyerService;
    private final StatisticDao statisticDao;

    @Override
    public Map<Integer, BuyerStatistic> getStatistics(StatisticFilterDto filter) {
        List<Statistic> statistics = statisticDao.getStatistics(filter);
        return groupStats(groupByBuyer(statistics));
    }

    @Override
    public Map<Integer, BuyerStatistic> getDailyStatistics(StatisticFilterDto filter) {
        List<Statistic> dailyStatistics = statisticDao.getDailyStatistics(filter);
        return groupStats(groupByBuyer(dailyStatistics));
    }

    @Override
    public Map<Integer, BuyerStatistic> getAllStatistics(StatisticFilterDto filter) {
        Map<Integer, List<Statistic>> stats = new HashMap<>();
        List<Statistic> statistics = statisticDao.getStatistics(filter);
        if (isFilterIncludeToday(filter.getTo())) {
            Map<Integer, List<Statistic>> dailyStats = groupByBuyer(statisticDao.getDailyStatistics(filter));
            stats = updateAllStats(stats, dailyStats);
        }
        Map<Integer, List<Statistic>> allStats = groupByBuyer(statistics);
        return groupStats(updateAllStats(stats, allStats));
    }

    private Map<Integer, BuyerStatistic> groupStats(Map<Integer, List<Statistic>> stats) {
        Map<Integer, BuyerStatistic> buyerStatistic = new HashMap<>();

        for (Map.Entry<Integer, List<Statistic>> entry : stats.entrySet()) {
            BuyerStatistic buyerCost = createBuyerStatistic(entry.getKey(), entry.getValue());
            buyerCost.setBuyerName(buyerService.getBuyerById(entry.getKey()));
            buyerStatistic.put(entry.getKey(), buyerCost);
        }
        return buyerStatistic;
    }
}
