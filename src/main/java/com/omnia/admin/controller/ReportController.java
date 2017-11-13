package com.omnia.admin.controller;

import com.google.common.collect.Lists;
import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.service.ExcelReportService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("report")
@AllArgsConstructor
public class ReportController {

    private final ExcelReportService excelReportService;

    @GetMapping("stats")
    public ResponseEntity downloadStatsReport(@RequestParam String buyers,
                                              @RequestParam String from,
                                              @RequestParam String to,
                                              @RequestParam String types) throws IOException {
        StatisticFilter statisticFilter = new StatisticFilter();
        statisticFilter.setBuyers(Lists.newArrayList(StringUtils.commaDelimitedListToSet(buyers)));
        statisticFilter.setTypes(Lists.newArrayList(StringUtils.commaDelimitedListToSet(types)));
        statisticFilter.setFrom(from);
        statisticFilter.setTo(to);
        return fileResponse(excelReportService.create(statisticFilter));
    }

    private ResponseEntity fileResponse(File report) throws IOException {
        InputStreamResource resource = new InputStreamResource(new FileInputStream(report));
        return ResponseEntity.ok()
                .contentLength(report.length())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + report.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
