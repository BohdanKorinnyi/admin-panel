package com.omnia.admin.service;

import com.omnia.admin.dto.StatisticFilterDto;
import com.omnia.admin.model.BuyerStatistic;
import com.omnia.admin.model.Statistic;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.omnia.admin.service.impl.StatisticServiceImpl.EMPTY_STATS_MAP;
import static java.util.Objects.isNull;

public interface StatisticService {
    Map<Integer, BuyerStatistic> getStatistics(StatisticFilterDto filter);

    Map<Integer, BuyerStatistic> getDailyStatistics(StatisticFilterDto filter);

    Map<Integer, BuyerStatistic> getAllStatistics(StatisticFilterDto filter);

    default BuyerStatistic createBuyerStatistic(int buyerId, List<Statistic> statistics) {
        double sum = statistics.stream()
                .mapToDouble(Statistic::getSpent)
                .sum();

        BuyerStatistic buyerStatistic = new BuyerStatistic();
        buyerStatistic.setBuyerId(buyerId);
        buyerStatistic.setBuyerTotalSpent(sum);
        buyerStatistic.setStatistics(statistics);
        return buyerStatistic;
    }

    default Map<Integer, List<Statistic>> groupByBuyer(List<Statistic> statistics) {
        if (CollectionUtils.isEmpty(statistics)) {
            return EMPTY_STATS_MAP;
        }
        return statistics.stream()
                .collect(Collectors.groupingBy(Statistic::getBuyerId, Collectors.toList()));
    }

    default boolean isFilterIncludeToday(String toDate) {
        if (StringUtils.isEmpty(toDate)) {
            return false;
        }
        Date filterDate = Date.valueOf(toDate);
        Date today = Date.valueOf(LocalDate.now());
        return filterDate.equals(today) || filterDate.after(today);
    }

    default void updateAllStats(Map<Integer, List<Statistic>> stats, Map<Integer, List<Statistic>> newStats) {
        if (isNull(newStats) || newStats.isEmpty()) {
            return;
        }
        if (stats.isEmpty()) {
            stats = newStats;
            return;
        }

        for (Map.Entry<Integer, List<Statistic>> entry : newStats.entrySet()) {
            List<Statistic> statistics = stats.getOrDefault(entry.getKey(), new ArrayList<>());
            statistics.addAll(entry.getValue());
        }
    }
}
