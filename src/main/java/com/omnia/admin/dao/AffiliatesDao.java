package com.omnia.admin.dao;

import java.util.List;

public interface AffiliatesDao {
    List<Long> getAffiliatesIdsByBuyerId(long buyerId);
}
