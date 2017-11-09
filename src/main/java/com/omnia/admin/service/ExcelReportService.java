package com.omnia.admin.service;

import com.omnia.admin.dto.StatFilter;

public interface ExcelReportService {
    Object create(StatFilter filter);
}
