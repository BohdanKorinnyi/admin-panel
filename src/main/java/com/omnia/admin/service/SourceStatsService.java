package com.omnia.admin.service;

import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.BuyerStatistic;
import com.omnia.admin.model.SourceStatistic;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.omnia.admin.service.impl.SourceStatsServiceImpl.EMPTY_STATS_MAP;
import static java.util.Objects.isNull;

public interface SourceStatsService {
    Map<Integer, BuyerStatistic> getStatistics(StatisticFilter filter);

    Map<Integer, BuyerStatistic> getDailyStatistics(StatisticFilter filter);

    Map<Integer, BuyerStatistic> getAllStatistics(StatisticFilter filter);

    default BuyerStatistic createBuyerStatistic(int buyerId, List<SourceStatistic> sourceStatistics) {
        double sum = sourceStatistics.stream()
                .mapToDouble(SourceStatistic::getSpent)
                .sum();
        DecimalFormat twoDForm = new DecimalFormat("#,####");
        BuyerStatistic buyerStatistic = new BuyerStatistic();
        buyerStatistic.setBuyerId(buyerId);
        buyerStatistic.setBuyerTotalSpent(Double.valueOf(twoDForm.format(sum)));
        buyerStatistic.setSourceStatistics(sourceStatistics);
        return buyerStatistic;
    }

    default Map<Integer, List<SourceStatistic>> groupByBuyer(List<SourceStatistic> sourceStatistics) {
        if (CollectionUtils.isEmpty(sourceStatistics)) {
            return EMPTY_STATS_MAP;
        }
        return sourceStatistics.stream()
                .collect(Collectors.groupingBy(SourceStatistic::getBuyerId, Collectors.toList()));
    }

    default boolean isFilterIncludeToday(String toDate) {
        if (StringUtils.isEmpty(toDate)) {
            return true;
        }
        Date filterDate = Date.valueOf(toDate);
        Date today = Date.valueOf(LocalDate.now());
        return filterDate.equals(today) || filterDate.after(today);
    }

    default Map<Integer, List<SourceStatistic>> updateAllStats(Map<Integer, List<SourceStatistic>> stats, Map<Integer, List<SourceStatistic>> newStats) {
        if (isNull(newStats) || newStats.isEmpty()) {
            return stats;
        }
        if (stats.isEmpty()) {
            return newStats;
        }

        for (Map.Entry<Integer, List<SourceStatistic>> entry : newStats.entrySet()) {
            if (stats.containsKey(entry.getKey())) {
                List<SourceStatistic> sourceStatistics = stats.get(entry.getKey());
                sourceStatistics.addAll(entry.getValue());
                continue;
            }
            stats.put(entry.getKey(), entry.getValue());
        }
        return stats;
    }
}
