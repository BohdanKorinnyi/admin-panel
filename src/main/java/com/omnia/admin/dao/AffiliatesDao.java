package com.omnia.admin.dao;

import com.omnia.admin.model.Affiliates;

import java.util.List;

public interface AffiliatesDao {
    List<Affiliates> findAffiliates();

    List<Long> getAffiliatesIdsByBuyerId(long buyerId);

    List<Long> getAffiliatesIdsByBuyerId();

    void generate(long afid,long buyerId);
}
