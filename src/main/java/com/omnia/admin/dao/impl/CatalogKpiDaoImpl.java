package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.CatalogKpiDao;
import com.omnia.admin.model.CatalogKpi;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class CatalogKpiDaoImpl implements CatalogKpiDao {
    private static final String SELECT_CATALOGS_KPI = "SELECT * FROM catalog_kpi;";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<CatalogKpi> findCatalogs() {
        return jdbcTemplate.query(SELECT_CATALOGS_KPI, BeanPropertyRowMapper.newInstance(CatalogKpi.class));
    }
}
