package com.omnia.admin.service;

import com.omnia.admin.model.BuyerPlan;

import java.util.List;

@FunctionalInterface
public interface BuyerPlanService {
    List<BuyerPlan> getBuyerPlan(List<String> buyers, List<String> month);
}
