package com.omnia.admin.controller;

import com.omnia.admin.service.CatalogKpiService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CatalogKpiController {
    private final CatalogKpiService catalogKpiService;

    @GetMapping("catalogs/kpi")
    public ResponseEntity getCatalogKpi() {
        return ResponseEntity.ok(catalogKpiService.getCatalogKpi());
    }
}
