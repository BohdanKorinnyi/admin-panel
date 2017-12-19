package com.omnia.admin.service.impl;

import com.google.common.collect.Lists;
import com.omnia.admin.dao.SpentDao;
import com.omnia.admin.model.BuyerCosts;
import com.omnia.admin.model.SpentByDateReport;
import com.omnia.admin.model.SpentBySourceReport;
import com.omnia.admin.service.SpentService;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class SpentServiceImpl implements SpentService {
    private final SpentDao spentDao;

    @Override
    public Float getSpentByCurrentMonth(Integer buyerId) {
        return spentDao.calculateBuyerCurrentMonthSpent(buyerId);
    }

    @Override
    public Float getSpentByToday(Integer buyerId) {
        return spentDao.calculateBuyerTodaySpent(buyerId);
    }

    @Override
    public Float getSpentByYesterday(Integer buyerId) {
        return spentDao.calculateBuyerYesterdaySpent(buyerId);
    }

    @Override
    public List<SpentByDateReport> createSpentByBuyerReport(String from, String to) {
        List<BuyerCosts> costs = spentDao.createSpentByBuyerReport(from, to);
        Map<String, List<BuyerCosts>> groupedByDate = CollectionUtils.emptyIfNull(costs).stream()
                .collect(Collectors.groupingBy(BuyerCosts::getDate, toList()));
        List<SpentByDateReport> result = Lists.newArrayList();
        for (Map.Entry<String, List<BuyerCosts>> entry : groupedByDate.entrySet()) {
            Map<String, List<BuyerCosts>> groupedBySource = CollectionUtils.emptyIfNull(entry.getValue()).stream()
                    .collect(Collectors.groupingBy(BuyerCosts::getSource, toList()));
            List<SpentBySourceReport> spentBySourceReports = Lists.newArrayList();
            for (Map.Entry<String, List<BuyerCosts>> report : groupedBySource.entrySet()) {
                spentBySourceReports.add(new SpentBySourceReport(report.getKey(), report.getValue()));
            }
            result.add(new SpentByDateReport(entry.getKey(), spentBySourceReports));
        }
        return result;
    }
}
