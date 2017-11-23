package com.omnia.admin.dao;

import com.omnia.admin.model.BuyerKpi;

import java.util.List;

public interface BuyerKpiDao {
    List<BuyerKpi> findByBuyerId(int buyerId);

    void save(List<BuyerKpi> kpis, int buyerId);

    Float getBuyerRevenuePlan(Integer buyerId);

    Float getBuyerProfitPlan(Integer buyerId);
}
