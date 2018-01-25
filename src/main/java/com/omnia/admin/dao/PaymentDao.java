package com.omnia.admin.dao;

import com.omnia.admin.model.Payment;

import java.util.List;

@FunctionalInterface
public interface PaymentDao {
    List<Payment> getPayrollsByBuyerAndYear(int buyerId, int year);
}
