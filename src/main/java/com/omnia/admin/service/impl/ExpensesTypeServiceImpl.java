package com.omnia.admin.service.impl;

import com.omnia.admin.dao.ExpensesTypeDao;
import com.omnia.admin.model.ExpensesType;
import com.omnia.admin.service.ExpensesTypeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j
@Service
@AllArgsConstructor
public class ExpensesTypeServiceImpl implements ExpensesTypeService {
    private final ExpensesTypeDao expensesTypeDao;

    @Override
    public void save(String name) {
        expensesTypeDao.save(name);
    }

    @Override
    public void update(ExpensesType expensesType) {
        expensesTypeDao.update(expensesType);
    }

    @Override
    public List<ExpensesType> findAll() {
        return expensesTypeDao.findAll();
    }
}
