package com.omnia.admin.service.impl;

import com.google.common.collect.Lists;
import com.omnia.admin.dao.AffiliatesDao;
import com.omnia.admin.model.Affiliates;
import com.omnia.admin.service.AffiliatesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AffiliatesServiceImpl implements AffiliatesService {
    private final AffiliatesDao affiliatesDao;

    @Override
    public List<Affiliates> findAffiliates() {
        return affiliatesDao.findAffiliates();
    }

    @Override
    public List<Long> getAffiliatesIdsByBuyerId(long buyerId) {
        return affiliatesDao.getAffiliatesIdsByBuyerId(buyerId);
    }

    @Override
    public List<Long> generate(int quantity, long buyerId) {
        List<Long> ids = affiliatesDao.getAffiliatesIdsByBuyerId();
        List<Long> newAfIds = Lists.newArrayList();
        for (long i = 0; i < Long.MAX_VALUE; i++) {
            if (ids.contains(i)) {
                continue;
            }
            if (newAfIds.size() != quantity) {
                affiliatesDao.generate(i, buyerId);
                newAfIds.add(i);
                continue;
            }
            return newAfIds;
        }
        return newAfIds;
    }
}
