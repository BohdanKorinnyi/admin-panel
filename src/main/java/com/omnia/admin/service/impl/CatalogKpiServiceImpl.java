package com.omnia.admin.service.impl;

import com.omnia.admin.dao.CatalogKpiDao;
import com.omnia.admin.model.CatalogKpi;
import com.omnia.admin.service.CatalogKpiService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CatalogKpiServiceImpl implements CatalogKpiService {
    private final CatalogKpiDao catalogKpiDao;

    @Override
    public List<CatalogKpi> getCatalogKpi() {
        return catalogKpiDao.findCatalogs();
    }
}
