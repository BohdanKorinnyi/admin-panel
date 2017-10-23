package com.omnia.admin.grid.filter.impl;

import com.omnia.admin.grid.filter.Filter;

import static com.omnia.admin.grid.filter.FilterConstant.EQUALS;

public class ArbitratorIdFilter implements Filter {
    private static final String COLUMN = "conversions.afid";

    @Override
    public String getSql(String value) {
        return COLUMN + String.format(EQUALS, value);
    }
}
