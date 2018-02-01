package com.omnia.admin.dao;

import com.omnia.admin.model.Payment;
import com.omnia.admin.model.PaymentDto;

import java.util.List;

public interface PaymentDao {
    List<Payment> getByBuyerAndYear(int buyerId, int year);

    List<PaymentDto> getByBuyerIds(List<Integer> buyerIds);

    void save(List<Payment> payments);
}
