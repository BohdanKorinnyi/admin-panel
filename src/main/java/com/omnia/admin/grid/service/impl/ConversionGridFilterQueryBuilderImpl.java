package com.omnia.admin.grid.service.impl;

import com.omnia.admin.grid.dto.conversion.ConversionFilterDto;
import com.omnia.admin.grid.dto.conversion.ConversionGridFilterDetails;
import com.omnia.admin.grid.enums.conversion.ConversionFilterColumn;
import com.omnia.admin.grid.filter.Filter;
import com.omnia.admin.grid.filter.conversion.FilterFactory;
import com.omnia.admin.grid.service.ConversionGridFilterQueryBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.omnia.admin.grid.filter.FilterConstant.AND;
import static com.omnia.admin.grid.filter.FilterConstant.EMPTY;
import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class ConversionGridFilterQueryBuilderImpl implements ConversionGridFilterQueryBuilder {

    private final FilterFactory filterFactory;

    @Override
    public String createFilterQuery(ConversionGridFilterDetails filterDetails) {
        ConversionFilterDto filterDto = filterDetails.getFilter();
        if (isNull(filterDto) || CollectionUtils.isEmpty(filterDto.getFields())) {
            return EMPTY;
        }
        List<ConversionFilterColumn> conversionFilterColumns = filterDto.getFields();
        StringBuilder sqlBuilder = new StringBuilder();
        boolean isFirst = true;
        for (ConversionFilterColumn conversionFilterColumn : conversionFilterColumns) {
            Filter filter = filterFactory.getFilter(conversionFilterColumn);
            String filterValue = filterDto.getFilterValue(conversionFilterColumn);
            String sql = filter.getSql(filterValue);
            if (!StringUtils.isEmpty(sql)) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    sqlBuilder.append(AND);
                }
                sqlBuilder.append(sql);
            }
        }
        return sqlBuilder.toString();
    }
}
