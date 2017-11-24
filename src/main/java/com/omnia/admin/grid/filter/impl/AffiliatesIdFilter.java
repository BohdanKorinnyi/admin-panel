package com.omnia.admin.grid.filter.impl;

import com.omnia.admin.grid.filter.Filter;
import org.springframework.util.StringUtils;

import static com.omnia.admin.grid.filter.FilterConstant.EMPTY;
import static com.omnia.admin.grid.filter.FilterConstant.EQUALS;

public class AffiliatesIdFilter implements Filter {
    private static final String COLUMN = "conversions.afid";

    @Override
    public String getSql(String value) {
        if (StringUtils.isEmpty(value)) {
            return EMPTY;
        }
        return COLUMN + " IN ('" + value.replaceAll(",", "','") + "')";
    }
}
