package com.omnia.admin.service.impl;

import com.omnia.admin.dao.StatisticDao;
import com.omnia.admin.dto.StatisticFilterDto;
import com.omnia.admin.model.BuyerStatistic;
import com.omnia.admin.model.Statistic;
import com.omnia.admin.service.BuyerService;
import com.omnia.admin.service.StatisticService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private static final Map<Integer, BuyerStatistic> EMPTY_MAP = new HashMap<>();

    private final BuyerService buyerService;
    private final StatisticDao statisticDao;

    @Override
    public Map<Integer, BuyerStatistic> getStatistics(StatisticFilterDto filter) {
        List<Statistic> statistics = statisticDao.getStatistics(filter);
        if (CollectionUtils.isEmpty(statistics)) {
            return EMPTY_MAP;
        }
        Map<Integer, List<Statistic>> buyers = statistics.stream()
                .collect(Collectors.groupingBy(Statistic::getBuyerId, Collectors.toList()));
        Map<Integer, BuyerStatistic> buyerStatistic = new HashMap<>();

        for (Map.Entry<Integer, List<Statistic>> entry : buyers.entrySet()) {
            BuyerStatistic buyerCost = createBuyerStatistic(entry.getKey(), entry.getValue());
            buyerCost.setBuyerName(buyerService.getBuyerById(entry.getKey()));
            buyerStatistic.put(entry.getKey(), buyerCost);
        }
        return buyerStatistic;
    }
}
