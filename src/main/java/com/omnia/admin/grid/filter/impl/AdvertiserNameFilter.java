package com.omnia.admin.grid.filter.impl;

import com.omnia.admin.grid.filter.Filter;

import static com.omnia.admin.grid.filter.FilterConstant.LIKE;

public class AdvertiserNameFilter implements Filter {

    private static final String COLUMN = "adverts.advshortname";

    @Override
    public String getSql(String value) {
        return COLUMN + String.format(LIKE, value);
    }
}
