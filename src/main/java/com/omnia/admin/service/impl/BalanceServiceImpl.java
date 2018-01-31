package com.omnia.admin.service.impl;

import com.omnia.admin.dao.BalanceDao;
import com.omnia.admin.model.Balance;
import com.omnia.admin.service.BalanceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BalanceServiceImpl implements BalanceService {
    private final BalanceDao balanceDao;

    @Override
    public List<Balance> getMonthBalance(int year, String month, List<Long> advertiserIds) {
        return balanceDao.getMonthBalance(year, month, advertiserIds);
    }
}
