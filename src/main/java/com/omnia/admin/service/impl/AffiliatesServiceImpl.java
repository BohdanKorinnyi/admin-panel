package com.omnia.admin.service.impl;

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
    public void generate(int quantity, long buyerId) {
        List<Long> ids = affiliatesDao.getAffiliatesIdsByBuyerId();
        int counter = 0;
        for (long i = 0; i < Long.MAX_VALUE; i++) {
            if (ids.contains(i)) {
                continue;
            }
            if (counter != quantity) {
                counter++;
                affiliatesDao.generate(i, buyerId);
                continue;
            }
            return;
        }
    }
}
