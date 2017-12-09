package com.omnia.admin.dao;

import com.omnia.admin.grid.Page;
import com.omnia.admin.model.Payroll;

import java.util.List;

public interface PayrollDao {
    Integer countAll(Integer buyerId);

    List<Payroll> findPayrolls(Page page, Integer buyerId);

    List<Payroll> findPayrollsByBuyerId(Integer buyerId);

    void update(Payroll payroll);

    void save(List<Payroll> payrolls);

    void delete(List<Long> ids);

    List<String> getPayrollDescription();
}
