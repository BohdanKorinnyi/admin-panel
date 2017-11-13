package com.omnia.admin.service.impl;

import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.BuyerStatistic;
import com.omnia.admin.model.Expenses;
import com.omnia.admin.service.ExpensesService;
import com.omnia.admin.service.PostbackService;
import com.omnia.admin.service.SourceStatsService;
import com.omnia.admin.service.StatisticService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public final class StatisticServiceImpl implements StatisticService {
    private final SourceStatsService sourceStatsService;
    private final PostbackService postbackService;
    private final ExpensesService expensesService;

    @Override
    public Map<String, Object> getBuyerStatistics(StatisticFilter filter) {
        Map<Integer, BuyerStatistic> sources = sourceStatsService.getAllStatistics(filter);
        List<Expenses> expenses = expensesService.getExpenses(filter);
        return null;
    }
}
