package com.omnia.admin.service.impl;

import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.BuyerProjection;
import com.omnia.admin.model.statistic.SourcesResult;
import com.omnia.admin.model.Expenses;
import com.omnia.admin.model.statistic.Stats;
import com.omnia.admin.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j
@Service
@AllArgsConstructor
public final class StatisticServiceImpl implements StatisticService {
    private final SourceStatsService sourceStatsService;
    private final PostbackService postbackService;
    private final ExpensesService expensesService;
    private final BuyerService buyerService;

    @Override
    public List<Stats> getBuyerStatistics(StatisticFilter filter) {
        long start = System.currentTimeMillis();
        List<Expenses> expenses = expensesService.getExpenses(filter);
        log.info("Expenses loading from database: " + (System.currentTimeMillis() - start) + "ms");
        start = System.currentTimeMillis();
        List<PostbackStats> stats = postbackService.getStats(filter);
        log.info("PostbackResult loading from database: " + (System.currentTimeMillis() - start) + "ms");
        start = System.currentTimeMillis();
        Map<Integer, SourcesResult> statistics = sourceStatsService.getAllStatistics(filter);
        log.info("Buyers statistic loading from database: " + (System.currentTimeMillis() - start) + "ms");
        start = System.currentTimeMillis();
        Map<Integer, List<Expenses>> expensesByBuyerId = expenses.stream()
                .collect(Collectors.groupingBy(Expenses::getBuyerId, Collectors.toList()));
        Map<Integer, List<PostbackStats>> postbackByBuyerId = stats.stream()
                .collect(Collectors.groupingBy(PostbackStats::getBuyerId, Collectors.toList()));
        log.info("Grouping completed: " + (System.currentTimeMillis() - start) + "ms");
        log.info("Buyer creation..");
        start = System.currentTimeMillis();
        Map<Integer, Stats> allStatistic = groupResult(statistics, expensesByBuyerId, postbackByBuyerId);
        List<Stats> result = new ArrayList<>();
        for (Map.Entry<Integer, Stats> statsEntry : allStatistic.entrySet()) {
            String buyerName = buyerService.getBuyerById(statsEntry.getKey());
            BuyerProjection buyerProjection = new BuyerProjection(statsEntry.getKey(), buyerName);
            Stats value = statsEntry.getValue();
            value.setBuyerInfo(buyerProjection);
            result.add(value);
        }
        log.info("Buyer creation completed: " + (System.currentTimeMillis() - start));
        return result;
    }

    private Map<Integer, Stats> groupResult(Map<Integer, SourcesResult> statistics,
                                            Map<Integer, List<Expenses>> expensesByBuyerId,
                                            Map<Integer, List<PostbackStats>> postbackByBuyerId) {
        Map<Integer, Stats> allStatistic = new HashMap<>();
        for (Map.Entry<Integer, SourcesResult> tmp : statistics.entrySet()) {
            if (allStatistic.containsKey(tmp.getKey())) {
                allStatistic.get(tmp.getKey()).setSources(tmp.getValue());
            } else {
                Stats tmpStats = new Stats();
                tmpStats.setSources(tmp.getValue());
                allStatistic.put(tmp.getKey(), tmpStats);
            }
        }
        for (Map.Entry<Integer, List<Expenses>> tmp : expensesByBuyerId.entrySet()) {
            if (allStatistic.containsKey(tmp.getKey())) {
                allStatistic.get(tmp.getKey()).setExpenses(tmp.getValue());
            } else {
                Stats tmpStats = new Stats();
                tmpStats.setExpenses(tmp.getValue());
                allStatistic.put(tmp.getKey(), tmpStats);
            }
        }
        for (Map.Entry<Integer, List<PostbackStats>> tmp : postbackByBuyerId.entrySet()) {
            if (allStatistic.containsKey(tmp.getKey())) {
                allStatistic.get(tmp.getKey()).setPostbacks(tmp.getValue());
            } else {
                Stats tmpStats = new Stats();
                tmpStats.setPostbacks(tmp.getValue());
                allStatistic.put(tmp.getKey(), tmpStats);
            }
        }
        return allStatistic;
    }
}
