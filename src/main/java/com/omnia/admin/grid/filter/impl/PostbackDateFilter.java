package com.omnia.admin.grid.filter.impl;

import com.omnia.admin.grid.filter.Filter;

import static com.omnia.admin.grid.filter.FilterConstant.BETWEEN;
import static com.omnia.admin.grid.filter.FilterConstant.SPACE;

public class PostbackDateFilter implements Filter {

    private static final String COLUMN = "postback.date";

    @Override
    public String getSql(String value) {
        String[] dates = value.split(SPACE);
        return COLUMN + String.format(BETWEEN, dates[0], dates[1]);
    }
}
