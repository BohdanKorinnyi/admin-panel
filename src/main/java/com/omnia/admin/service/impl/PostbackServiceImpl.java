package com.omnia.admin.service.impl;

import com.omnia.admin.dao.PostbackDao;
import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.service.PostbackService;
import com.omnia.admin.service.PostbackStats;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostbackServiceImpl implements PostbackService {
    private final PostbackDao postbackDao;

    @Override
    public Optional<String> getFullUrl(Long postbackId) {
        return postbackDao.findFullUrlById(postbackId);
    }

    @Override
    public List<PostbackStats> getPostbackStats(StatisticFilter filter) {
        return postbackDao.getStats(filter);
    }

    @Override
    public float getRevenueByBuyer(int buyerId) {
        return 0;
    }
}
