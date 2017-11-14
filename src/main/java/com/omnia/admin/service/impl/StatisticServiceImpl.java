package com.omnia.admin.service.impl;

import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.*;
import com.omnia.admin.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public final class StatisticServiceImpl implements StatisticService {
    private final SourceStatsService sourceStatsService;
    private final PostbackService postbackService;
    private final ExpensesService expensesService;
    private final BuyerService buyerService;

    @Override
    public List<Stats> getBuyerStatistics(StatisticFilter filter) {
        List<Expenses> expenses = expensesService.getExpenses(filter);
        List<PostbackStats> stats = postbackService.getStats(filter);

        Map<Integer, BuyerStatistic> statistics = sourceStatsService.getAllStatistics(filter);
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
        List<Stats> result = new ArrayList<>();
        for (Map.Entry<Integer, Stats> statsEntry : allStatistic.entrySet()) {
            String buyerName = buyerService.getBuyerById(statsEntry.getKey());
            BuyerProjection buyerProjection = new BuyerProjection(statsEntry.getKey(), buyerName);
            Stats value = statsEntry.getValue();
            value.setBuyerInfo(buyerProjection);
            result.add(value);
        }
        return result;
    }
}
