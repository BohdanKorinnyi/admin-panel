package com.omnia.admin.service;

import java.util.List;

@FunctionalInterface
public interface BuyerPlanService {
    List<Object> getBuyerPlan(List<String> months, List<String> buyers);
}
