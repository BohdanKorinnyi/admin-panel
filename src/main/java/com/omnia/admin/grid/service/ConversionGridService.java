package com.omnia.admin.grid.service;

import com.omnia.admin.grid.dto.conversion.ConversionGridFilterDetails;
import com.omnia.admin.grid.dto.conversion.ConversionList;

public interface ConversionGridService {
    ConversionList getConversions(ConversionGridFilterDetails filterDetails);
}
