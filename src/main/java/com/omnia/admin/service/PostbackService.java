package com.omnia.admin.service;

import com.omnia.admin.model.Postback;
import com.omnia.admin.model.Revenue;

import java.util.List;
import java.util.Optional;

public interface PostbackService {
    List<Revenue> getRevenueByBuyerIdAndYear(long buyerId, int year);

    Float getRevenueByPeriod(List<Integer> advertiserIds, String from, String to);

    Optional<String> getFullUrl(Long postbackId);

    Float getRevenueByBuyer(int buyerId);

    Float getTodayRevenueByBuyer(int buyerId);

    Float getYesterdayRevenueByBuyer(int buyerId);

    List<Postback> getPostbacksByConversionId(long conversionId);
}
