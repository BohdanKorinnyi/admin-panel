package com.omnia.admin.service;

import com.omnia.admin.model.SpentByDateReport;

import java.util.List;

public interface SpentService {
    Float getSpentByCurrentMonth(Integer buyerId);

    Float getSpentByToday(Integer buyerId);

    Float getSpentByYesterday(Integer buyerId);

    List<SpentByDateReport> createSpentByBuyerReport(String from, String to);
}
