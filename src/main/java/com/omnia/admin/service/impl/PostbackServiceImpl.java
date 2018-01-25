package com.omnia.admin.service.impl;

import com.omnia.admin.dao.PostbackDao;
import com.omnia.admin.model.Postback;
import com.omnia.admin.model.Revenue;
import com.omnia.admin.service.PostbackService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostbackServiceImpl implements PostbackService {
    private final PostbackDao postbackDao;

    @Override
    public List<Revenue> getRevenueByBuyerIdAndYear(long buyerId, int year) {
        return postbackDao.getRevenueByBuyerIdAndYear(buyerId, year);
    }

    @Override
    public Float getRevenueByPeriod(List<Integer> advertiserIds, String from, String to) {
        return postbackDao.getRevenueByPeriod(advertiserIds,from, to);
    }

    @Override
    public Optional<String> getFullUrl(Long postbackId) {
        return postbackDao.findFullUrlById(postbackId);
    }

    @Override
    public Float getRevenueByBuyer(int buyerId) {
        return postbackDao.getRevenueByBuyer(buyerId);
    }

    @Override
    public Float getTodayRevenueByBuyer(int buyerId) {
        return postbackDao.getTodayRevenueByBuyer(buyerId);
    }

    @Override
    public Float getYesterdayRevenueByBuyer(int buyerId) {
        return postbackDao.getYesterdayRevenueByBuyer(buyerId);
    }

    @Override
    public List<Postback> getPostbacksByConversionId(long conversionId) {
        return postbackDao.findPostbackByConversionId(conversionId);
    }
}
