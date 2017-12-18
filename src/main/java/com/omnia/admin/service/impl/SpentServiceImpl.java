package com.omnia.admin.service.impl;

import com.omnia.admin.dao.SpentDao;
import com.omnia.admin.service.SpentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
}
