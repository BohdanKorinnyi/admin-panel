package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.ProcessedPostbackDao;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@AllArgsConstructor
public class ProcessedPostbackDaoImpl implements ProcessedPostbackDao {

    private static final String INSERT_PROCESSED_POSTBACK_KEY = "INSERT INTO processed_postback (postback_key) VALUES (?)";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean isPostbackProcessed(String postbackId) {
        return false;
    }

    @Override
    @Transactional
    public void save(List<String> postbackKeys) {
        jdbcTemplate.batchUpdate(INSERT_PROCESSED_POSTBACK_KEY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, postbackKeys.get(i));
            }
            @Override
            public int getBatchSize() {
                return postbackKeys.size();
            }
        });
    }
}
