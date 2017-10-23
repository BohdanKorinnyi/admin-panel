package com.omnia.admin.grid.service.impl;

import com.omnia.admin.grid.dto.postback.PostbackFilterDto;
import com.omnia.admin.grid.dto.postback.PostbackGridFilterDetails;
import com.omnia.admin.grid.enums.postback.PostbackFilterColumn;
import com.omnia.admin.grid.filter.Filter;
import com.omnia.admin.grid.filter.postback.PostbackFilterFactory;
import com.omnia.admin.grid.service.PostbackGridFilterQueryBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.omnia.admin.grid.filter.FilterConstant.AND;
import static com.omnia.admin.grid.filter.FilterConstant.EMPTY;
import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class PostbackGridFilterQueryBuilderImpl implements PostbackGridFilterQueryBuilder {

    private final PostbackFilterFactory postbackFilterFactory;

    @Override
    public String createFilterQuery(PostbackGridFilterDetails filterDetails) {
        PostbackFilterDto filterDto = filterDetails.getFilter();
        if (isNull(filterDto) || CollectionUtils.isEmpty(filterDto.getFields())) {
            return EMPTY;
        }
        List<PostbackFilterColumn> postbackFilterColumns = filterDto.getFields();
        StringBuilder sqlBuilder = new StringBuilder();
        boolean isFirst = true;
        for (PostbackFilterColumn postbackFilterColumn : postbackFilterColumns) {
            Filter filter = postbackFilterFactory.getFilter(postbackFilterColumn);
            String filterValue = filterDto.getFilterValue(postbackFilterColumn);
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
