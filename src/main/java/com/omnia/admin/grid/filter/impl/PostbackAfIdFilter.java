package com.omnia.admin.grid.filter.impl;

import com.omnia.admin.grid.filter.Filter;

import static com.omnia.admin.grid.filter.FilterConstant.BETWEEN;
import static com.omnia.admin.grid.filter.FilterConstant.EQUALS;
import static com.omnia.admin.grid.filter.FilterConstant.SPACE;

public class PostbackAfIdFilter implements Filter {

    private static final String COLUMN = "postback.afid";

    @Override
    public String getSql(String value) {
        return COLUMN + String.format(EQUALS, value);
    }
}
