package com.omnia.admin.service;

import com.omnia.admin.model.Payment;

import java.util.List;

public interface PaymentService {
    List<Payment> getByBuyerAndYear(int buyerId, int year);

    List<Payment> getByBuyer(List<Integer> buyerIds);

    void save(List<Payment> payments);
}
