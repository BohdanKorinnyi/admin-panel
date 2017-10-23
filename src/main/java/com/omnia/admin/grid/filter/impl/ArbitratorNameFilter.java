package com.omnia.admin.grid.filter.impl;

import com.omnia.admin.grid.filter.Filter;

import static com.omnia.admin.grid.filter.FilterConstant.LIKE;

public class ArbitratorNameFilter implements Filter {
    private static final String COLUMN = "affiliates.afname";
    @Override
    public String getSql(String value) {
        return COLUMN + String.format(LIKE, value);
    }
}
