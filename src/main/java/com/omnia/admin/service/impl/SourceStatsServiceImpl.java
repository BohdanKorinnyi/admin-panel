package com.omnia.admin.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.omnia.admin.controller.BuyerSource;
import com.omnia.admin.dao.SourceStatisticDao;
import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.Source;
import com.omnia.admin.model.SourceStat;
import com.omnia.admin.model.statistic.SourcesResult;
import com.omnia.admin.service.BuyerService;
import com.omnia.admin.service.SourceStatsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public final class SourceStatsServiceImpl implements SourceStatsService {
    public static final Map<Integer, List<Source>> EMPTY_STATS_MAP = ImmutableMap.of();
    private final BuyerService buyerService;
    private final SourceStatisticDao sourceStatisticDao;

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
    public List<BuyerSource> getSourceStat(List<Integer> buyerIds, String from, String to) {
        List<SourceStat> sourceStat = sourceStatisticDao.getSourceStat(buyerIds, from, to);
        Map<String, List<SourceStat>> collect = sourceStat.stream()
                .collect(Collectors.groupingBy(SourceStat::getBuyer, toList()));
        List<BuyerSource> sources = Lists.newArrayList();
        for (Map.Entry<String, List<SourceStat>> entry : collect.entrySet()) {
            BuyerSource buyerSource = new BuyerSource();
            buyerSource.setBuyer(entry.getKey());
            buyerSource.setData(entry.getValue());
            if (!CollectionUtils.isEmpty(entry.getValue())) {
                buyerSource.setBuyerId(entry.getValue().get(0).getBuyerId());
            }
            sources.add(buyerSource);
        }
        return sources;
    }

    @Override
    public List<SourceStat> getSourceStatByDate(Integer buyerId, String date) {
        return sourceStatisticDao.getSourceStatByDate(buyerId, date);
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
