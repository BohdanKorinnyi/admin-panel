package com.omnia.admin.grid.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omnia.admin.grid.dao.PostbackGridDao;
import com.omnia.admin.grid.dto.postback.PostbackGridFilterDetails;
import com.omnia.admin.grid.dto.postback.PostbackGridSortingDetails;
import com.omnia.admin.grid.dto.postback.PostbackList;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static com.omnia.admin.grid.filter.FilterConstant.*;
import static java.util.Objects.nonNull;

@Log4j
@Repository
@AllArgsConstructor
public class PostbackGridDaoImpl implements PostbackGridDao {
    private static final String ORDER_BY = "ORDER BY %s %s";
    private static final String SELECT_POSTBACK = "SELECT " +
            "  buyers.name AS buyer, " +
            "  postback.* " +
            "FROM postback " +
            "  LEFT JOIN affiliates ON affiliates.afid = postback.afid " +
            "  LEFT JOIN buyers ON affiliates.buyer_id = buyers.id " +
            "  JOIN adverts ON postback.advname = adverts.advshortname " +
            "  LEFT JOIN adv_status ON postback.status = adv_status.name ";
    private static final String SELECT_POSTBACK_COUNT = "SELECT COUNT(*) " +
            "FROM postback " +
            "  LEFT JOIN affiliates ON affiliates.afid = postback.afid " +
            "  LEFT JOIN buyers ON affiliates.buyer_id = buyers.id " +
            "  JOIN adverts ON postback.advname = adverts.advshortname " +
            "  LEFT JOIN adv_status ON postback.status = adv_status.name ";

    private final JdbcTemplate jdbcTemplate;

    @Override
    @SneakyThrows
    public PostbackList getPostbacks(PostbackGridFilterDetails filterDetails, String whereQuery) {
        if (!StringUtils.isEmpty(whereQuery)) {
            whereQuery = WHERE + whereQuery;
        }
        String whereWithSortQuery = whereQuery + getOrderBy(filterDetails.getSortingDetails());
        String sql = SELECT_POSTBACK + whereWithSortQuery + getLimit(filterDetails);
//        long start = System.currentTimeMillis();
        List<Map<String, Object>> postbacks = jdbcTemplate.queryForList(sql);
//        log.info("Select postbacks executed in " + (System.currentTimeMillis() - start) + "ms, sql=" + sql);
        Integer count = jdbcTemplate.queryForObject(SELECT_POSTBACK_COUNT + whereWithSortQuery, Integer.class);
        return new PostbackList(count, postbacks);
    }

    private String getLimit(PostbackGridFilterDetails filterDetails) {
        return " GROUP BY postback.id " + String.format(LIMIT, filterDetails.getFirstElement(), filterDetails.getSize());
    }

    private String getOrderBy(PostbackGridSortingDetails sortingDetails) {
        return nonNull(sortingDetails) && sortingDetails.hasSortingDetails() ?
                String.format(ORDER_BY, sortingDetails.getColumn().getName(), sortingDetails.getOrder().name())
                : EMPTY;
    }
}
