package com.omnia.admin.service.impl;

import com.omnia.admin.dao.PayrollDao;
import com.omnia.admin.grid.Page;
import com.omnia.admin.model.Payroll;
import com.omnia.admin.model.PayrollType;
import com.omnia.admin.service.PayrollService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PayrollServiceImpl implements PayrollService {
    private final PayrollDao payrollDao;

    @Override
    public List<Payroll> getPayrollsByBuyerAndYear(int buyerId, int year) {
        return payrollDao.getPayrollsByBuyerAndYear(buyerId, year);
    }

    @Override
    public Integer countAll(Integer buyerId) {
        return payrollDao.countAll(buyerId);
    }

    @Override
    public List<Payroll> findPayrolls(Page page, Integer buyerId) {
        return payrollDao.findPayrolls(page, buyerId);
    }

    @Override
    public List<Payroll> findPayrollsByBuyerId(Integer buyerId) {
        return payrollDao.findPayrollsByBuyerId(buyerId);
    }

    @Override
    public void update(Payroll payroll) {
        payrollDao.update(payroll);
    }

    @Override
    public void save(List<Payroll> payrolls) {
        payrollDao.save(payrolls);
    }

    @Override
    public void delete(List<Long> ids) {
        payrollDao.delete(ids);
    }

    @Override
    public List<String> getPayrollDescription() {
        return payrollDao.getPayrollDescription();
    }

    @Override
    public List<PayrollType> getTypes() {
        return payrollDao.getTypes();
    }

    @Override
    public List<Payroll> getPayrollsByStaffId(int staffId) {
        return payrollDao.getPayrollsByStaffId(staffId);
    }
}
