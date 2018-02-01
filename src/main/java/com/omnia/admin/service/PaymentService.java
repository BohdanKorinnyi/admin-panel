package com.omnia.admin.service;

import com.omnia.admin.model.Payment;
import com.omnia.admin.model.PaymentDto;

import java.util.List;

public interface PaymentService {
    List<Payment> getByBuyerAndYear(int buyerId, int year);

    List<PaymentDto> getByBuyer(List<Integer> buyerIds);

    void save(List<Payment> payments);
}
