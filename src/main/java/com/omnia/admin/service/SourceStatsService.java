package com.omnia.admin.service;

import com.omnia.admin.controller.BuyerSource;
import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.Source;
import com.omnia.admin.model.SourceStat;
import com.omnia.admin.model.statistic.SourcesResult;
import org.apache.commons.collections4.MapUtils;
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
import static java.util.Objects.nonNull;

public interface SourceStatsService {
    Map<Integer, SourcesResult> getDailyAndGeneralStatistics(StatisticFilter filter);

    List<BuyerSource> getSourceStat(List<Integer> buyerIds, String from, String to);

    List<SourceStat> getSourceStatByDate(Integer buyerId, String date);

    Float getProfitByBuyerId(int buyerId);

    default SourcesResult createBuyerStatistic(int buyerId, List<Source> sources) {
        double sum = sources.stream()
                .mapToDouble(Source::getSpent)
                .sum();
        SourcesResult sourcesResult = new SourcesResult();
        sourcesResult.setId(buyerId);
        sourcesResult.setSum(sum);
        sourcesResult.setData(sources);
        return sourcesResult;
    }

    default Map<Integer, List<Source>> groupByBuyer(List<Source> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return EMPTY_STATS_MAP;
        }
        return sources.stream()
                .filter(source -> nonNull(source.getBuyerId()))
                .collect(Collectors.groupingBy(Source::getBuyerId, Collectors.toList()));
    }

    default boolean isFilterIncludeToday(String toDate) {
        if (StringUtils.isEmpty(toDate)) {
            return true;
        }
        Date filterDate = Date.valueOf(toDate);
        Date today = Date.valueOf(LocalDate.now());
        return filterDate.equals(today) || filterDate.after(today);
    }

    default Map<Integer, List<Source>> updateAllStats(Map<Integer, List<Source>> stats, Map<Integer, List<Source>> newStats) {
        if (MapUtils.isEmpty(newStats)) {
            return stats;
        }
        if (stats.isEmpty()) {
            return newStats;
        }

        for (Map.Entry<Integer, List<Source>> entry : newStats.entrySet()) {
            if (stats.containsKey(entry.getKey())) {
                List<Source> sources = stats.get(entry.getKey());
                sources.addAll(entry.getValue());
                continue;
            }
            stats.put(entry.getKey(), entry.getValue());
        }
        return stats;
    }
}
