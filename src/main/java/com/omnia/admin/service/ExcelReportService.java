package com.omnia.admin.service;

import com.omnia.admin.dto.SourceStatFilter;

import java.io.File;

public interface ExcelReportService {
    File create(SourceStatFilter filter);
}
