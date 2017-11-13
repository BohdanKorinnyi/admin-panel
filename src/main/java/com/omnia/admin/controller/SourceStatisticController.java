package com.omnia.admin.controller;

import com.omnia.admin.dto.SourceStatFilter;
import com.omnia.admin.service.ExcelReportService;
import com.omnia.admin.service.SourceStatsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("statistic")
public class SourceStatisticController {

    private final SourceStatsService sourceStatsService;
    private final ExcelReportService excelReportService;


    @PostMapping
    public ResponseEntity getStatistic(@RequestBody SourceStatFilter filter) {
        return ResponseEntity.ok(sourceStatsService.getStatistics(filter));
    }

    @PostMapping("daily")
    public ResponseEntity getDailyStatistic(@RequestBody SourceStatFilter filter) {
        return ResponseEntity.ok(sourceStatsService.getDailyStatistics(filter));
    }

    @PostMapping("all")
    public ResponseEntity getAllStatistic(@RequestBody SourceStatFilter filter) {
        return ResponseEntity.ok(sourceStatsService.getAllStatistics(filter));
    }
}
