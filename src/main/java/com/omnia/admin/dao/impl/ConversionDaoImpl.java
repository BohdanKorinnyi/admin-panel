package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.ConversionDao;
import com.omnia.admin.model.Conversion;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Log4j
@Repository
@AllArgsConstructor
public class ConversionDaoImpl implements ConversionDao {

    private static final String SELECT_CONVERSION_BY_PREFIX_AND_CLICK_ID = "SELECT * FROM conversion WHERE prefix = ? AND clickid = ?;";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Conversion> findConversionByPrefixAndClickId(String prefix, String clickId) {
        try {
            Conversion conversion = jdbcTemplate.queryForObject(SELECT_CONVERSION_BY_PREFIX_AND_CLICK_ID,
                    new BeanPropertyRowMapper<>(Conversion.class), prefix, clickId);
            return Optional.of(conversion);
        } catch (Exception e) {
            log.error("Error occurred during searching conversion by prefix=" + prefix + " and clickid=" + clickId, e);
        }
        return Optional.empty();
    }
}
