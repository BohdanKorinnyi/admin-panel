package com.omnia.admin.grid.filter.impl;


import com.omnia.admin.grid.filter.Filter;

import static com.omnia.admin.grid.filter.FilterConstant.EMPTY;

public class TimeZoneFilter implements Filter {
    @Override
    public String getSql(String value) {
        return EMPTY;
    }
}
