package com.omnia.admin.service.impl;

import com.omnia.admin.dao.CurrencyDao;
import com.omnia.admin.model.Currency;
import com.omnia.admin.service.CurrencyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyDao currencyDao;

    @Override
    public List<Currency> getCurrencies() {
        return currencyDao.getAllCurrency();
    }
}
