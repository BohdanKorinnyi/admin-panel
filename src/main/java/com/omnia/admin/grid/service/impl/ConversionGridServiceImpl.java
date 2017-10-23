package com.omnia.admin.grid.service.impl;

import com.omnia.admin.grid.dao.ConversionGridDao;
import com.omnia.admin.grid.dto.conversion.ConversionGridFilterDetails;
import com.omnia.admin.grid.dto.conversion.ConversionList;
import com.omnia.admin.grid.service.ConversionGridFilterQueryBuilder;
import com.omnia.admin.grid.service.ConversionGridService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConversionGridServiceImpl implements ConversionGridService {

    private final ConversionGridDao conversionGridDao;
    private final ConversionGridFilterQueryBuilder conversionGridFilterQueryBuilder;

    @Override
    public ConversionList getConversions(ConversionGridFilterDetails filterDetails) {
        String filterQuery = conversionGridFilterQueryBuilder.createFilterQuery(filterDetails);
        return conversionGridDao.getConversions(filterDetails, filterQuery);
    }
}
