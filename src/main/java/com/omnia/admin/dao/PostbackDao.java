package com.omnia.admin.dao;

import com.omnia.admin.model.Postback;

import java.util.List;
import java.util.Optional;

public interface PostbackDao {
    Float getRevenueByPeriod(List<Integer> advertiserIds, String from, String to);

    Optional<String> findFullUrlById(Long postbackId);

    Float getRevenueByBuyer(int buyerId);

    Float getTodayRevenueByBuyer(int buyerId);

    Float getYesterdayRevenueByBuyer(int buyerId);

    List<Postback> findPostbackByConversionId(long conversionId);
}
