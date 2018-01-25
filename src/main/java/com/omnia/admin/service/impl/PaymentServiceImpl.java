package com.omnia.admin.service.impl;

import com.omnia.admin.dao.PaymentDao;
import com.omnia.admin.model.Payment;
import com.omnia.admin.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentDao paymentDao;

    @Override
    public List<Payment> getPaymentByBuyerAndYear(int buyerId, int year) {
        return paymentDao.getPayrollsByBuyerAndYear(buyerId, year);
    }
}
