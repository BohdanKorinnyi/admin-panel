package com.omnia.admin.grid.service;

import com.omnia.admin.grid.dto.conversion.ConversionGridFilterDetails;

@FunctionalInterface
public interface ConversionGridFilterQueryBuilder {
    String createFilterQuery(ConversionGridFilterDetails filterDetails);
}
