package com.omnia.admin.controller;

import com.omnia.admin.dto.StatFilter;
import com.omnia.admin.service.ExcelReportService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("report")
@AllArgsConstructor
public class ReportController {

    private final ExcelReportService excelReportService;

    @PostMapping("stats")
    public ResponseEntity downloadStatsReport(@RequestBody StatFilter filter) throws IOException {
        return fileResponse(excelReportService.create(filter));
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
