package com.omnia.admin.service;

import com.omnia.admin.model.Affiliates;

import java.util.List;

public interface AffiliatesService {
    List<Affiliates> findAffiliates();

    List<Long> getAffiliatesIdsByBuyerId(long buyerId);

    void generate(int quantity, long buyerId);
}
