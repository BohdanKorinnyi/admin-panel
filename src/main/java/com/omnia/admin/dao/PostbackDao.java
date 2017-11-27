package com.omnia.admin.dao;

import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.service.PostbackStats;

import java.util.List;
import java.util.Optional;

public interface PostbackDao {
    Optional<String> findFullUrlById(Long postbackId);

    List<PostbackStats> getStats(StatisticFilter filter);

    Float getRevenueByBuyer(int buyerId);
}
