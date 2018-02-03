package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.StaffDao;
import com.omnia.admin.model.Staff;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class StaffDaoImpl implements StaffDao {
    private static final String SELECT_STAFF = "SELECT * FROM staff";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Staff> findAll() {
        return jdbcTemplate.query(SELECT_STAFF, BeanPropertyRowMapper.newInstance(Staff.class));
    }
}
