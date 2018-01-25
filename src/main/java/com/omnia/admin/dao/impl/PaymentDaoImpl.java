package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.PaymentDao;
import com.omnia.admin.model.Payment;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class PaymentDaoImpl implements PaymentDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Payment> getPayrollsByBuyerAndYear(int buyerId, int year) {
        return null;
    }
}
