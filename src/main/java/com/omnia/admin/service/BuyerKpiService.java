package com.omnia.admin.service;

import com.omnia.admin.model.BuyerKpi;

import java.util.List;

public interface BuyerKpiService {
    List<BuyerKpi> getByBuyerId(int buyerId);

    void save(List<BuyerKpi> kpis, int buyerId);

    Float getBuyerRevenuePlan(Integer buyerId);

    Float getBuyerProfitPlan(Integer buyerId);
}
