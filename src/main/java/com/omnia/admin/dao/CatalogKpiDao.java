package com.omnia.admin.dao;

import com.omnia.admin.model.CatalogKpi;

import java.util.List;

@FunctionalInterface
public interface CatalogKpiDao {
    List<CatalogKpi> findCatalogs();
}
