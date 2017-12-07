package com.omnia.admin.service;

import com.omnia.admin.model.BuyerPlan;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface BuyerPlanService {
    Float getBuyerRevenuePlan(Integer buyerId);

    Float getMarginality(Integer buyerId) throws ExecutionException, InterruptedException;

    List<BuyerPlan> getBuyerPlan(List<Integer> buyers, List<String> month) throws ExecutionException, InterruptedException;
}
