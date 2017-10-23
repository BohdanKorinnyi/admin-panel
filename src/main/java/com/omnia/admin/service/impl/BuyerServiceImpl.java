package com.omnia.admin.service.impl;

import com.omnia.admin.dao.BuyerDao;
import com.omnia.admin.service.BuyerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BuyerServiceImpl implements BuyerService {
    private final BuyerDao buyerDao;

    @Override
    public List<String> getBuyersName() {
        return buyerDao.getBuyersName();
    }
}
