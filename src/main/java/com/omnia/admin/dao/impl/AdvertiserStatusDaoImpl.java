package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.AdvertiserStatusDao;
import com.omnia.admin.dto.AdvertiserStatusDto;
import com.omnia.admin.model.AdvertiserStatus;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Log4j
@Repository
public class AdvertiserStatusDaoImpl implements AdvertiserStatusDao {

    private static final String SELECT_STATUS_BY_ADVERTISER_ID = "SELECT * FROM adv_status WHERE adv_id = ?;";
    private static final String DELETE_STATUSES_FOR_ADVERTISER = "DELETE FROM adv_status WHERE adv_id = ?;";
    private static final String DELETE_STATUS_FOR_ADVERTISER = "DELETE FROM adv_status WHERE id = ? AND adv_id = ?;";
    private static final String INSERT_UPDATED_STATUS = "INSERT INTO adv_status (id, adv_id, name, type, real_status) VALUES (?, ?, ?, ?, ?);";
    private static final String UPDATE_STATUS = "UPDATE adv_status SET adv_id = ?, name = ?, type = ?, real_status = ? WHERE id = ?;";
    private static final String INSERT_NEW_STATUS = "INSERT INTO adv_status (adv_id, name, type, real_status) VALUES (?, ?, ?, ?);";

    private final JdbcTemplate jdbcTemplate;

    public AdvertiserStatusDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<AdvertiserStatus> getStatusListByAdvertiserId(long advertiserId) {
        return jdbcTemplate.query(SELECT_STATUS_BY_ADVERTISER_ID, new BeanPropertyRowMapper<>(AdvertiserStatus.class), advertiserId);
    }

    @Override
    public void update(AdvertiserStatusDto status, long advertiserId) {
        try {
            jdbcTemplate.update(UPDATE_STATUS, status.getAdvId(), status.getName(), status.getType(), status.getRealStatus(), status.getId());
        } catch (Exception e) {
            log.error("Error occurred during execution " + UPDATE_STATUS + " statusId=" + status.getId() + " advertiserId=" + status.getAdvId(), e);
            throw new RuntimeException();
        }
    }

    @Override
    public void save(AdvertiserStatusDto status, long advertiserId) {
        jdbcTemplate.update(INSERT_NEW_STATUS, advertiserId, status.getName(), status.getType(), status.getRealStatus());
    }

    @Override
    public void deleteAdvertiserStatuses(long advertiserId) {
        try {
            jdbcTemplate.update(DELETE_STATUSES_FOR_ADVERTISER, advertiserId);
        } catch (Exception e) {
            log.error("Error occurred during execution " + DELETE_STATUSES_FOR_ADVERTISER + " and advertiserId=" + advertiserId, e);
            throw new RuntimeException();
        }
    }
}
