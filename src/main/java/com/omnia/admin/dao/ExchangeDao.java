package com.omnia.admin.dao;

import com.omnia.admin.model.ExchangeRate;

public interface ExchangeDao {
    ExchangeRate findExchangeRateByCurrencyCode(String code);
}
