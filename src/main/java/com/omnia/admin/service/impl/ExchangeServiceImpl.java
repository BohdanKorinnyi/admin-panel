package com.omnia.admin.service.impl;

import com.omnia.admin.dao.ExchangeDao;
import com.omnia.admin.model.ExchangeRate;
import com.omnia.admin.service.ExchangeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {

    private final ExchangeDao exchangeDao;

    @Override
    public float convertToDollar(String currencyCode, float sum) {
        ExchangeRate exchangeRate = exchangeDao.findExchangeRateByCurrencyCode(currencyCode);
        float rate = exchangeRate.getRate() / exchangeRate.getCount();
        return sum / rate;
    }
}
