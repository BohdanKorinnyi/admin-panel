package com.omnia.admin.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.omnia.admin.dao.BuyerDao;
import com.omnia.admin.model.Buyer;
import com.omnia.admin.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class BuyerServiceImpl implements BuyerService {
    private LoadingCache<Integer, String> buyerNameCache;
    private final BuyerDao buyerDao;

    @Autowired
    public BuyerServiceImpl(BuyerDao buyerDao) {
        this.buyerDao = buyerDao;
    }

    @PostConstruct
    public void init() {
        buyerNameCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build(new CacheLoader<Integer, String>() {
                    @Override
                    public String load(Integer buyerId) throws Exception {
                        return buyerDao.getBuyerById(buyerId);
                    }
                });
    }

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

    @Override
    public String getBuyerById(int buyerId) {
        return buyerNameCache.getUnchecked(buyerId);
    }
}
