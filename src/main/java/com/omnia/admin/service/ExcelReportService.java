package com.omnia.admin.service;

import com.omnia.admin.dto.StatisticFilter;

import java.io.File;

public interface ExcelReportService {
    File create(StatisticFilter filter);
}
