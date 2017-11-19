package com.omnia.admin.service.impl;

import com.omnia.admin.dao.BuyerKpiDao;
import com.omnia.admin.model.BuyerKpi;
import com.omnia.admin.service.BuyerKpiService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BuyerKpiServiceImpl implements BuyerKpiService {
    private final BuyerKpiDao buyerKpiDao;

    @Override
    public void save(List<BuyerKpi> kpis, int buyerId) {
        buyerKpiDao.save(kpis, buyerId);
    }

    @Override
    public Object getBuyerPlanInfo() {
        return null;
    }

    @Override
    public List<BuyerKpi> getByBuyerId(int buyerId) {
        return buyerKpiDao.findByBuyerId(buyerId);
    }
}
