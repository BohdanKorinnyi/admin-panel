package com.omnia.admin.service;

import com.omnia.admin.grid.Page;
import com.omnia.admin.model.Payroll;
import com.omnia.admin.model.PayrollType;

import java.util.List;

public interface PayrollService {
    List<Payroll> getPayrollsByBuyerAndYear(int buyerId, int year);

    Integer countAll(Integer buyerId);

    List<Payroll> findPayrolls(Page page, Integer buyerId);

    List<Payroll> findPayrollsByBuyerId(Integer buyerId);

    void update(Payroll payroll);

    void save(List<Payroll> payrolls);

    void delete(List<Long> ids);

    List<String> getPayrollDescription();

    List<PayrollType> getTypes();

    List<Payroll> getPayrollsByStaffId(int staffId);
}
