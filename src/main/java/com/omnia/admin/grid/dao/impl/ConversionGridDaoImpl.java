package com.omnia.admin.grid.dao.impl;

import com.omnia.admin.grid.dao.ConversionGridDao;
import com.omnia.admin.grid.dto.conversion.ConversionDto;
import com.omnia.admin.grid.dto.conversion.ConversionGridFilterDetails;
import com.omnia.admin.grid.dto.conversion.ConversionGridSortingDetails;
import com.omnia.admin.grid.dto.conversion.ConversionList;
import com.omnia.admin.grid.enums.conversion.ConversionGridColumn;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.omnia.admin.grid.filter.FilterConstant.*;
import static java.util.Objects.nonNull;

@Log4j
@Repository
@AllArgsConstructor
public class ConversionGridDaoImpl implements ConversionGridDao {

    private static final String CONVERSION_COLUMNS = ConversionGridColumn.getColumnList();
    private static final String ORDER_BY = "ORDER BY %s %s";

    private static final String SELECT_CONVERSIONS = "SELECT " + CONVERSION_COLUMNS +
            " FROM conversions" +
            "  LEFT JOIN affiliates ON affiliates.afid = conversions.afid" +
            "  LEFT JOIN buyers ON affiliates.buyer_id = buyers.id " +
            "  LEFT JOIN currency ON currency.id = conversions.currency_id" +
            "  LEFT JOIN adv_status ON adv_status.id = conversions.status_id" +
            "  LEFT JOIN adverts ON adverts.id = adv_status.adv_id" +
            "  LEFT JOIN offers_tracker ON offers_tracker.offer_id_tracker = conversions.offers_tracker_id" +
            "  LEFT JOIN offers_adv ON offers_adv.id = offers_tracker.offer_id_adv" +
            "  LEFT JOIN sources_tracker ON sources_tracker.id = conversions.source_id_tracker" +
            "  LEFT JOIN exchange ON exchange.id = conversions.exchange_id ";

    private static final String COUNT_CONVERSIONS = "SELECT COUNT(*)" +
            " FROM conversions" +
            "  LEFT JOIN affiliates ON affiliates.afid = conversions.afid" +
            "  LEFT JOIN buyers ON affiliates.buyer_id = buyers.id " +
            "  LEFT JOIN currency ON currency.id = conversions.currency_id" +
            "  LEFT JOIN adv_status ON adv_status.id = conversions.status_id" +
            "  LEFT JOIN adverts ON adverts.id = adv_status.adv_id" +
            "  LEFT JOIN offers_tracker ON offers_tracker.offer_id_tracker = conversions.offers_tracker_id" +
            "  LEFT JOIN offers_adv ON offers_adv.id = offers_tracker.offer_id_adv" +
            "  LEFT JOIN sources_tracker ON sources_tracker.id = conversions.source_id_tracker" +
            "  LEFT JOIN exchange ON exchange.id = conversions.exchange_id ";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public ConversionList getConversions(ConversionGridFilterDetails filterDetails, String whereQuery) {
        if (!StringUtils.isEmpty(whereQuery)) {
            whereQuery = WHERE + whereQuery;
        }
        String whereWithSortQuery = whereQuery + getOrderBy(filterDetails.getSortingDetails());
        String sql = SELECT_CONVERSIONS + whereWithSortQuery + getLimit(filterDetails);
        long start = System.currentTimeMillis();
        List<ConversionDto> conversionDtos = jdbcTemplate.query(sql, new ConversionDtoRowMapper());
        log.info("Select conversions executed in " + (System.currentTimeMillis() - start) + "ms, sql=" + sql);
        String sizeSql = COUNT_CONVERSIONS + whereWithSortQuery;
        start = System.currentTimeMillis();
        Integer size = jdbcTemplate.queryForObject(sizeSql, Integer.class);
        log.info("Select quantity of conversions executed in " + (System.currentTimeMillis() - start) + "ms, sql=" + sizeSql);
        return new ConversionList(size, conversionDtos);
    }

    private String getLimit(ConversionGridFilterDetails filterDetails) {
        return String.format(LIMIT, filterDetails.getFirstElement(), filterDetails.getSize());
    }

    private String getOrderBy(ConversionGridSortingDetails sortingDetails) {
        return nonNull(sortingDetails) && sortingDetails.hasSortingDetails() ?
                String.format(ORDER_BY, sortingDetails.getColumn().getValue(), sortingDetails.getOrder().name())
                : EMPTY;
    }

    private class ConversionDtoRowMapper implements RowMapper<ConversionDto> {
        @Override
        public ConversionDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConversionDto conversionDto = new ConversionDto();
            conversionDto.setConversionId(rs.getInt("id"));
            conversionDto.setPrefix(rs.getString("prefix"));
            conversionDto.setAfId(rs.getInt("afid"));
            conversionDto.setBuyer(rs.getString("afname"));
            conversionDto.setDate(rs.getDate("data_creation"));
            conversionDto.setPayout(rs.getFloat("sum"));
            conversionDto.setStatus(rs.getString("real_status"));
            conversionDto.setAdvert(rs.getString("advshortname"));
            conversionDto.setOfferId(rs.getString("offers_adv.name"));
            conversionDto.setTrafficSource(rs.getString("sources_tracker.name"));
            conversionDto.setRate(rs.getInt("rate"));
            conversionDto.setChange(rs.getDate("date_change"));
            conversionDto.setClickId(rs.getString("clickid"));
            conversionDto.setCode(rs.getString("code"));
            return conversionDto;
        }
    }
}
