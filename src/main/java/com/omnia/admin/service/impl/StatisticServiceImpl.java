package com.omnia.admin.service.impl;

import com.google.common.math.Stats;
import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.service.ExpensesService;
import com.omnia.admin.service.PostbackService;
import com.omnia.admin.service.SourceStatsService;
import com.omnia.admin.service.StatisticService;
import com.sun.org.glassfish.external.statistics.Statistic;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public final class StatisticServiceImpl implements StatisticService {
    private final SourceStatsService sourceStatsService;
    private final PostbackService postbackService;
    private final ExpensesService expensesService;

    @Override
    public Map<String, Object> getBuyerStatistics(StatisticFilter filter) {
        Map<String, Object> result = new HashMap<>();

        result.put("sources", sourceStatsService.getAllStatistics(filter));
        result.put("expenses", expensesService.getExpenses(filter));
        result.put("postbacks", postbackService.getStats(filter));
        return result;
    }
}
