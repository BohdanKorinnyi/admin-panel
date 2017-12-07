package com.omnia.admin.service.impl;

import com.google.common.collect.Lists;
import com.omnia.admin.dao.BuyerPlanDao;
import com.omnia.admin.model.BuyerPlan;
import com.omnia.admin.service.BuyerPlanService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class BuyerPlanServiceImpl implements BuyerPlanService {
    private static final String KPI_PROFIT_TYPE = "Profit";
    private static final String KPI_REVENUE_TYPE = "Revenue";
    private final BuyerPlanDao buyerPlanDao;

    @Override
    public Float getBuyerRevenuePlan(Integer buyerId) {
        return buyerPlanDao.getBuyerRevenuePlan(buyerId);
    }

    @Override
    public Float getMarginality(Integer buyerId) throws ExecutionException, InterruptedException {
        String month = LocalDateTime.now().getMonth().name();
        CompletableFuture<List<BuyerPlan>> revenuePlanFuture =
                supplyAsync(() -> buyerPlanDao.getBuyerRevenuePlan(Lists.newArrayList(buyerId), Lists.newArrayList(month)));
        CompletableFuture<List<BuyerPlan>> profitPlanFuture =
                supplyAsync(() -> buyerPlanDao.getBuyerProfitPlan(Lists.newArrayList(buyerId), Lists.newArrayList(month)));
        CompletableFuture.allOf(revenuePlanFuture, profitPlanFuture);
        List<BuyerPlan> buyerRevenuePlan = revenuePlanFuture.get();
        List<BuyerPlan> buyerProfitPlan = profitPlanFuture.get();
        if (CollectionUtils.isEmpty(buyerRevenuePlan) || CollectionUtils.isEmpty(buyerProfitPlan)) {
            return 0F;
        }
        Float profit = buyerProfitPlan.get(0).getSum();
        Float revenue = buyerRevenuePlan.get(0).getSum();
        return profit != 0 ? revenue / profit : 0;
    }

    @Override
    public List<BuyerPlan> getBuyerPlan(List<Integer> buyers, List<String> month) throws ExecutionException, InterruptedException {
        CompletableFuture<List<BuyerPlan>> revenuePlanFuture = supplyAsync(() -> buyerPlanDao.getBuyerRevenuePlan(buyers, month));
        CompletableFuture<List<BuyerPlan>> profitPlanFuture = supplyAsync(() -> buyerPlanDao.getBuyerProfitPlan(buyers, month));
        CompletableFuture.allOf(revenuePlanFuture, profitPlanFuture);
        List<BuyerPlan> buyerRevenuePlan = revenuePlanFuture.get();
        List<BuyerPlan> buyerProfitPlan = profitPlanFuture.get();
        List<BuyerPlan> groupedRevenue = groupRevenueByMonthAndBuyer(buyerRevenuePlan);
        buyerProfitPlan.addAll(groupedRevenue);
        return calculatePerformanceProfit(buyerProfitPlan);
    }

    private List<BuyerPlan> groupRevenueByMonthAndBuyer(List<BuyerPlan> plans) {
        Function<BuyerPlan, String> gropingFunction = plan -> plan.getBuyerName() + "_" + plan.getMonth();
        Map<String, List<BuyerPlan>> groupedPlans = plans.stream()
                .collect(Collectors.groupingBy(gropingFunction, toList()));
        List<BuyerPlan> result = Lists.newArrayList();
        for (Map.Entry<String, List<BuyerPlan>> plan : groupedPlans.entrySet()) {
            List<BuyerPlan> value = plan.getValue();
            if (value.size() > 1) {
                result.add(sumBuyerPlan(value));
            } else {
                result.add(value.get(0));
            }
        }
        return result;
    }

    private BuyerPlan sumBuyerPlan(List<BuyerPlan> plans) {
        float sum = 0;
        BuyerPlan resultPlan = plans.get(0);
        for (BuyerPlan plan : plans) {
            sum = sum + plan.getSum();
        }
        resultPlan.setSum(sum);
        resultPlan.setPerformance(calculatePerformance(resultPlan.getKpiValue(), sum));
        return resultPlan;
    }

    private float calculatePerformance(float plan, float sum) {
        if (sum == 0 || plan == 0) {
            return 0;
        }
        return (sum / plan) * 100;
    }

    private List<BuyerPlan> calculatePerformanceProfit(List<BuyerPlan> plans) {
        for (BuyerPlan plan : plans) {
            if (KPI_PROFIT_TYPE.equals(plan.getKpiName())) {
                Float revenue = getRevenueSumByMonth(plans, plan.getMonth(), plan.getBuyerName());
                plan.setSum(revenue - plan.getSum());
                plan.setPerformance(calculatePerformance(plan.getKpiValue(), plan.getSum()));
            }
        }
        return plans;
    }

    private float getRevenueSumByMonth(List<BuyerPlan> plans, String month, String buyerName) {
        return plans.stream()
                .filter(plan -> KPI_REVENUE_TYPE.equals(plan.getKpiName()) && plan.getMonth().equals(month) && plan.getBuyerName().equals(buyerName))
                .findFirst()
                .map(BuyerPlan::getSum)
                .orElse(0F);
    }
}
