package com.omnia.admin.service;

import com.omnia.admin.dto.StatisticFilterDto;
import com.omnia.admin.model.BuyerStatistic;
import com.omnia.admin.model.Statistic;

import java.util.List;
import java.util.Map;

public interface StatisticService {
    Map<Integer, BuyerStatistic> getStatistics(StatisticFilterDto filter);

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
}
