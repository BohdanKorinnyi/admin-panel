package com.omnia.admin.dao;

import com.omnia.admin.model.Payment;

import java.util.List;

public interface PaymentDao {
    List<Payment> getByBuyerAndYear(int buyerId, int year);

    List<Payment> getByBuyerIds(List<Integer> buyerIds);

    void save(List<Payment> payments);
}
