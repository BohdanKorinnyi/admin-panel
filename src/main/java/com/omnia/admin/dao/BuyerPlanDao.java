package com.omnia.admin.dao;

import com.omnia.admin.model.BuyerPlan;

import java.util.List;

public interface BuyerPlanDao {
    List<BuyerPlan> getBuyerRevenuePlan(List<String> buyers, List<String> month);

    List<BuyerPlan> getBuyerProfitPlan(List<String> buyers, List<String> month);
}
