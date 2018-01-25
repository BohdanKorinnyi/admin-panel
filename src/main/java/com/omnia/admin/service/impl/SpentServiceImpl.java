package com.omnia.admin.service.impl;

import com.omnia.admin.dao.SpentDao;
import com.omnia.admin.model.BuyerCosts;
import com.omnia.admin.model.Spent;
import com.omnia.admin.service.SpentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SpentServiceImpl implements SpentService {
    private final SpentDao spentDao;

    @Override
    public List<Spent> getSpentByBuyerAndYear(int buyer, int year) {
        return spentDao.getSpentByBuyerAndYear(buyer, year);
    }

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
    public List<BuyerCosts> getSpentReport(List<Integer> buyerIds, List<String> sources, String from, String to) {
        return spentDao.getSpentReport(buyerIds, sources, from, to);
    }
}
