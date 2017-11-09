package com.omnia.admin.controller;

import com.omnia.admin.dto.StatFilter;
import com.omnia.admin.service.ExcelReportService;
import com.omnia.admin.service.StatisticService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("statistic")
public class StatisticController {

    private final StatisticService statisticService;
    private final ExcelReportService excelReportService;


    @PostMapping
    public ResponseEntity getStatistic(@RequestBody StatFilter filter) {
        return ResponseEntity.ok(statisticService.getStatistics(filter));
    }

    @PostMapping("daily")
    public ResponseEntity getDailyStatistic(@RequestBody StatFilter filter) {
        return ResponseEntity.ok(statisticService.getDailyStatistics(filter));
    }

    @PostMapping("all")
    public ResponseEntity getAllStatistic(@RequestBody StatFilter filter) {
        return ResponseEntity.ok(statisticService.getAllStatistics(filter));
    }
}
