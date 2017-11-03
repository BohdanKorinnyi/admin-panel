package com.omnia.admin.dao;

import java.util.List;

public interface AffiliatesDao {
    List<Long> getAffiliatesIdsByBuyerId(long buyerId);

    List<Long> getAffiliatesIdsByBuyerId();

    void generate(long afid,long buyerId);
}
