package com.omnia.admin.service.impl;

import com.omnia.admin.dao.BuyerPlanDao;
import com.omnia.admin.model.BuyerPlan;
import com.omnia.admin.service.BuyerPlanService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BuyerPlanServiceImpl implements BuyerPlanService {
    private final BuyerPlanDao buyerPlanDao;

    @Override
    public List<BuyerPlan> getBuyerPlan() {
        List<BuyerPlan> buyerProfitPlan = buyerPlanDao.getBuyerProfitPlan();
        List<BuyerPlan> buyerRevenuePlan = buyerPlanDao.getBuyerRevenuePlan();
        buyerProfitPlan.addAll(buyerRevenuePlan);
        return buyerProfitPlan;
    }
}
