package com.omnia.admin.service;

import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.BuyerPlan;

import java.util.List;
import java.util.Optional;

public interface PostbackService {
    Optional<String> getFullUrl(Long postbackId);

    List<PostbackStats> getPostbackStats(StatisticFilter filter);

    float getRevenueByBuyer(int buyerId);
}
