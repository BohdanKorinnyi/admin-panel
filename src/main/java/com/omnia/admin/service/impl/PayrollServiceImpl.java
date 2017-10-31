package com.omnia.admin.service.impl;

import com.omnia.admin.dao.PayrollDao;
import com.omnia.admin.grid.Page;
import com.omnia.admin.model.Payroll;
import com.omnia.admin.service.PayrollService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PayrollServiceImpl implements PayrollService {
    private final PayrollDao payrollDao;

    @Override
    public Integer countAll() {
        return payrollDao.countAll();
    }

    @Override
    public List<Payroll> findPayrolls(Page page) {
        return payrollDao.findPayrolls(page);
    }

    @Override
    public void update(List<Payroll> payrolls) {
        payrollDao.update(payrolls);
    }

    @Override
    public void save(List<Payroll> payrolls) {
        payrollDao.save(payrolls);
    }

    @Override
    public void delete(List<Long> ids) {
        payrollDao.delete(ids);
    }
}
