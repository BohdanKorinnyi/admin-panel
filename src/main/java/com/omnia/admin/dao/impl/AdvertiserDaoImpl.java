package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.AdvertiserDao;
import com.omnia.admin.dto.AdvertiserDto;
import com.omnia.admin.model.Advertiser;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

import static java.util.Objects.isNull;

@Repository
@AllArgsConstructor
public class AdvertiserDaoImpl implements AdvertiserDao {

    private static final String SELECT_ALL_ADVERTISERS = "SELECT * FROM adverts;";
    private static final String SELECT_ALL_ADVERTISER_NAMES = "SELECT advshortname FROM adverts ORDER BY advshortname ASC;";
    private static final String INSERT_ADVERTISER = "INSERT INTO adverts (advname, advshortname, secretkey, url, api_key, risk, timezone) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String UPDATE_ADVERTISER = "UPDATE adverts SET advname = ?, advshortname = ?, secretkey = ?, url = ?, risk = ?, api_key = ?, timezone = ? WHERE id = ?;";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Advertiser> findAll() {
        return jdbcTemplate.query(SELECT_ALL_ADVERTISERS, new BeanPropertyRowMapper<>(Advertiser.class));
    }

    @Override
    public List<String> getAllAdvertisersName() {
        return jdbcTemplate.queryForList(SELECT_ALL_ADVERTISER_NAMES, String.class);
    }

    @Override
    public Number save(AdvertiserDto advertiser) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_ADVERTISER, new String[]{"id"});
            ps.setString(1, advertiser.getAdvname());
            ps.setString(2, advertiser.getAdvshortname());
            ps.setString(3, advertiser.getSecretKey());
            ps.setString(4, advertiser.getUrl());
            ps.setString(5, advertiser.getApiKey());
            ps.setLong(6, isNull(advertiser.getRisk()) ? 0 : advertiser.getRisk());
            ps.setInt(7, advertiser.getTimezone());
            return ps;
        }, keyHolder);
        return keyHolder.getKey();
    }

    @Override
    public void update(AdvertiserDto advertiser) {
        jdbcTemplate.update(UPDATE_ADVERTISER, advertiser.getAdvname(), advertiser.getAdvshortname(),
                advertiser.getSecretKey(), advertiser.getUrl(), advertiser.getRisk(),
                advertiser.getApiKey(), advertiser.getTimezone(), advertiser.getId()
        );
    }
}
