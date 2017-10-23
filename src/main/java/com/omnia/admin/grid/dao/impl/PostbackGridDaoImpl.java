package com.omnia.admin.grid.dao.impl;

import com.omnia.admin.grid.dao.PostbackGridDao;
import com.omnia.admin.grid.dto.postback.PostbackGridFilterDetails;
import com.omnia.admin.grid.dto.postback.PostbackGridSortingDetails;
import com.omnia.admin.grid.dto.postback.PostbackList;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static com.omnia.admin.grid.filter.FilterConstant.*;
import static java.util.Objects.nonNull;

@Repository
@AllArgsConstructor
public class PostbackGridDaoImpl implements PostbackGridDao {
    private static final String ORDER_BY = "ORDER BY %s %s";
    private static final String SELECT_POSTBACK = "SELECT " +
            "  affiliates.afname AS buyer, " +
            "  postback.* " +
            "FROM postback " +
            "  LEFT JOIN affiliates ON affiliates.afid = postback.afid ";
    private static final String SELECT_POSTBACK_COUNT = "SELECT COUNT(*) " +
            "FROM postback " +
            "  LEFT JOIN affiliates ON affiliates.afid = postback.afid ";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public PostbackList getPostbacks(PostbackGridFilterDetails filterDetails, String whereQuery) {
        if (!StringUtils.isEmpty(whereQuery)) {
            whereQuery = WHERE + whereQuery;
        }
        String whereWithSortQuery = whereQuery + getOrderBy(filterDetails.getSortingDetails());
        System.out.println(SELECT_POSTBACK + whereWithSortQuery + getLimit(filterDetails));
        List<Map<String, Object>> postbacks = jdbcTemplate.queryForList(SELECT_POSTBACK + whereWithSortQuery + getLimit(filterDetails));
        Integer count = jdbcTemplate.queryForObject(SELECT_POSTBACK_COUNT + whereWithSortQuery, Integer.class);

        return new PostbackList(count, postbacks);
    }

    private String getLimit(PostbackGridFilterDetails filterDetails) {
        return String.format(LIMIT, filterDetails.getFirstElement(), filterDetails.getSize());
    }

    private String getOrderBy(PostbackGridSortingDetails sortingDetails) {
        return nonNull(sortingDetails) && sortingDetails.hasSortingDetails() ?
                String.format(ORDER_BY, sortingDetails.getColumn().getName(), sortingDetails.getOrder().name())
                : EMPTY;
    }
}
