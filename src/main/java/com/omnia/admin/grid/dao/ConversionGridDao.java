package com.omnia.admin.grid.dao;

import com.omnia.admin.grid.dto.conversion.ConversionGridFilterDetails;
import com.omnia.admin.grid.dto.conversion.ConversionList;

public interface ConversionGridDao {
    ConversionList getConversions(ConversionGridFilterDetails filterDetails, String whereQuery);
}
