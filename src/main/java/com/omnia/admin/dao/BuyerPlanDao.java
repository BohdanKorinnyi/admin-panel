package com.omnia.admin.dao;

import com.omnia.admin.model.BuyerPlan;

import java.util.List;

public interface BuyerPlanDao {
    List<BuyerPlan> getBuyerProfitPlan();

    List<BuyerPlan> getBuyerRevenuePlan();
}
