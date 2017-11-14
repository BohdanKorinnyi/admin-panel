package com.omnia.admin.service.impl;

import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.BuyerProjection;
import com.omnia.admin.model.BuyerStatistic;
import com.omnia.admin.model.Expenses;
import com.omnia.admin.model.Stats;
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
        log.info("Expenses loading: " + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        List<PostbackStats> stats = postbackService.getStats(filter);
        log.info("Postback loading: " + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        Map<Integer, BuyerStatistic> statistics = sourceStatsService.getAllStatistics(filter);
        log.info("Buyers loading: " + (System.currentTimeMillis() - start));

        log.info("Grouping start...");
        start = System.currentTimeMillis();
        Map<Integer, List<Expenses>> expensesByBuyerId = expenses.stream()
                .collect(Collectors.groupingBy(Expenses::getBuyerId, Collectors.toList()));
        Map<Integer, List<PostbackStats>> postbackByBuyerId = stats.stream()
                .collect(Collectors.groupingBy(PostbackStats::getBuyerId, Collectors.toList()));

        Map<Integer, Stats> allStatistic = new HashMap<>();
        for (Map.Entry<Integer, BuyerStatistic> tmp : statistics.entrySet()) {
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
        log.info("Grouping completed: " + (System.currentTimeMillis() - start));
        log.info("Buyer creation..");
        start = System.currentTimeMillis();
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
}
