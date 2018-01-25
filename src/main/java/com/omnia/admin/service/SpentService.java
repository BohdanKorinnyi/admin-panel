package com.omnia.admin.service;

import com.omnia.admin.model.BuyerCosts;
import com.omnia.admin.model.Spent;

import java.util.List;

public interface SpentService {
    List<Spent> getSpentByBuyerAndYear(int buyer, int year);

    Float getSpentByCurrentMonth(Integer buyerId);

    Float getSpentByToday(Integer buyerId);

    Float getSpentByYesterday(Integer buyerId);

    List<BuyerCosts> getSpentReport(List<Integer> buyerIds, List<String> sources, String from, String to);
}
