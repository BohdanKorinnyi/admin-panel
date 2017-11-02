package com.omnia.admin.service.impl;

import com.omnia.admin.dao.AffiliatesDao;
import com.omnia.admin.service.AffiliatesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AffiliatesServiceImpl implements AffiliatesService {
    private final AffiliatesDao affiliatesDao;

    @Override
    public List<Long> getAffiliatesIdsByBuyerId(long buyerId) {
        return null;
    }
}
