package com.omnia.admin.service.impl;

import com.omnia.admin.dao.IncomeDao;
import com.omnia.admin.model.Income;
import com.omnia.admin.service.IncomeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IncomeServiceImpl implements IncomeService {
    private final IncomeDao incomeDao;
    @Override
    public void save(Income income) {
        incomeDao.save(income);
    }
}
