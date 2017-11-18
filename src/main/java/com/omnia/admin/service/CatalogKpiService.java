package com.omnia.admin.service;

import com.omnia.admin.model.CatalogKpi;

import java.util.List;

@FunctionalInterface
public interface CatalogKpiService {
    List<CatalogKpi> getCatalogKpi();
}
