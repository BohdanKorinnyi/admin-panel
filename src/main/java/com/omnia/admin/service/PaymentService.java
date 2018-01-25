package com.omnia.admin.service;

import com.omnia.admin.model.Payment;

import java.util.List;

@FunctionalInterface
public interface PaymentService {
    List<Payment> getPaymentByBuyerAndYear(int buyerId, int year);
}
