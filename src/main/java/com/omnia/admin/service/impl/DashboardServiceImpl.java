package com.omnia.admin.service.impl;

import com.omnia.admin.model.Payroll;
import com.omnia.admin.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Service
@AllArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final PostbackService postbackService;
    private final PayrollService payrollService;
    private final StatisticService statisticService;
    private final BuyerKpiService buyerKpiService;

    @Override
    public Map<String, Object> getDashboardData(Integer buyerId) throws ExecutionException, InterruptedException {
        HashMap<String, Object> response = new HashMap<>();
        CompletableFuture<Float> revenueFuture = supplyAsync(() -> postbackService.getRevenueByBuyer(buyerId));
        CompletableFuture<List<Payroll>> payrolls = supplyAsync(() -> payrollService.findPayrollsByBuyerId(buyerId));
        CompletableFuture<Float> profitFuture = supplyAsync(() -> statisticService.getProfitByBuyer(buyerId));
        CompletableFuture<Float> revenuePlanFuture = supplyAsync(() -> buyerKpiService.getBuyerProfitPlan(buyerId));
        CompletableFuture<Float> profitPlanFuture = supplyAsync(() -> buyerKpiService.getBuyerRevenuePlan(buyerId));

        CompletableFuture.allOf(revenueFuture, payrolls, profitFuture, revenuePlanFuture, profitPlanFuture);
        Float revenue = revenueFuture.get();
        Float profit = profitFuture.get();
        response.put("revenue", revenue);
        response.put("revenuePlan", revenuePlanFuture.get());
        response.put("profit", revenue - profit);
        response.put("profitPlan", profitPlanFuture.get());
        response.put("payroll", payrolls.get());
        response.put("totalPaid", payrolls.get().parallelStream().mapToDouble(Payroll::getSum).sum());
        response.put("bonus", profit > 0 ? profit * 0.2 : 0);
        return response;
    }
}
