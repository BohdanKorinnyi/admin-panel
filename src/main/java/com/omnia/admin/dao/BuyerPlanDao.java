package com.omnia.admin.dao;

import com.omnia.admin.model.BuyerPlan;
import io.swagger.models.auth.In;

import java.util.List;

public interface BuyerPlanDao {
    List<BuyerPlan> getBuyerRevenuePlan(List<Integer> buyers, List<String> month);

    List<BuyerPlan> getBuyerProfitPlan(List<Integer> buyers, List<String> month);
}
