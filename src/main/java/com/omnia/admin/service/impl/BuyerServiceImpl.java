package com.omnia.admin.service.impl;

import com.omnia.admin.dao.BuyerDao;
import com.omnia.admin.model.Buyer;
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

    @Override
    public List<Buyer> getBuyers() {
        return buyerDao.getBuyers();
    }

    @Override
    public void saveBuyers(List<Buyer> buyers) {
        buyerDao.saveBuyers(buyers);
    }

    @Override
    public void updateBuyers(List<Buyer> buyers) {
        buyerDao.updateBuyers(buyers);
    }
}
