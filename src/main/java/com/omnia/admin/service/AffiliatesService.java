package com.omnia.admin.service;

import java.util.List;

public interface AffiliatesService {
    List<Long> getAffiliatesIdsByBuyerId(long buyerId);

    void generate(int quantity, long buyerId);
}
