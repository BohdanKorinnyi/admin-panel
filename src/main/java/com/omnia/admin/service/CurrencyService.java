package com.omnia.admin.service;

import com.omnia.admin.model.Currency;

import java.util.List;

public interface CurrencyService {
    List<Currency> getCurrencies();

    float convertToDollar(String currencyCode, float sum);
}
