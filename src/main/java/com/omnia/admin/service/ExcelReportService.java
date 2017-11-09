package com.omnia.admin.service;

import com.omnia.admin.dto.StatFilter;

import java.io.File;

public interface ExcelReportService {
    File create(StatFilter filter);
}
