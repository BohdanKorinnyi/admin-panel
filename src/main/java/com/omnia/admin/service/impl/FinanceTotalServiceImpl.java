package com.omnia.admin.service.impl;

import com.omnia.admin.dao.FinanceTotalDao;
import com.omnia.admin.model.FinanceTotal;
import com.omnia.admin.service.FinanceTotalService;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class FinanceTotalServiceImpl implements FinanceTotalService {
    private final FinanceTotalDao financeTotalDao;

    @Override
    public Map<String, List<FinanceTotal>> getFinanceTotal(String from, String to) {
        List<FinanceTotal> financeTotalByTimeRange = financeTotalDao.findFinanceTotalByTimeRange(from, to);
        return groupByBuyer(financeTotalByTimeRange);
    }

    private Map<String, List<FinanceTotal>> groupByBuyer(List<FinanceTotal> financeTotals) {
        return CollectionUtils.emptyIfNull(financeTotals).stream()
                .collect(Collectors.groupingBy(FinanceTotal::getBuyer, toList()));
    }
}
