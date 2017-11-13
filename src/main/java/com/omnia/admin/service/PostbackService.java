package com.omnia.admin.service;

import com.omnia.admin.dto.StatisticFilter;

import java.util.List;
import java.util.Optional;

public interface PostbackService {
    Optional<String> getFullUrl(Long postbackId);

    List<PostbackStats> getStats(StatisticFilter filter);
}
