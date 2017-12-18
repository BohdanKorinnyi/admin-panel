package com.omnia.admin.service.impl;

import com.omnia.admin.dao.DashboardDao;
import com.omnia.admin.model.BuyerProfit;
import com.omnia.admin.model.Payroll;
import com.omnia.admin.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.util.Objects.isNull;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@Service
@AllArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final PostbackService postbackService;
    private final PayrollService payrollService;
    private final SourceStatsService sourceStatsService;
    private final BuyerKpiService buyerKpiService;
    private final BuyerService buyerService;
    private final DashboardDao dashboardDao;
    private final SpentService spentService;

    @Override
    public Map<String, Object> getDashboardData(Integer buyerId) throws ExecutionException, InterruptedException {
        HashMap<String, Object> response = new HashMap<>();
        CompletableFuture<Float> revenueFuture = supplyAsync(() -> postbackService.getRevenueByBuyer(buyerId));
        CompletableFuture<List<Payroll>> payrolls = supplyAsync(() -> payrollService.findPayrollsByBuyerId(buyerId));
        CompletableFuture<Float> profitFuture = supplyAsync(() -> sourceStatsService.getProfitByBuyerId(buyerId));
        CompletableFuture<Float> revenuePlanFuture = supplyAsync(() -> buyerKpiService.getBuyerProfitPlan(buyerId));
        CompletableFuture<Float> profitPlanFuture = supplyAsync(() -> buyerKpiService.getBuyerRevenuePlan(buyerId));

        CompletableFuture.allOf(revenueFuture, payrolls, profitFuture, revenuePlanFuture, profitPlanFuture);
        Float revenue = revenueFuture.get();
        Float profit = profitFuture.get();
        Float realProfit = (isNull(revenue) ? 0F : revenue) - (isNull(profit) ? 0F : profit);
        response.put("revenue", revenue);
        response.put("buyerName", buyerService.getBuyerById(buyerId));
        response.put("revenuePlan", revenuePlanFuture.get());
        response.put("profit", realProfit);
        response.put("profitPlan", profitPlanFuture.get());
        response.put("payroll", payrolls.get());
        response.put("revenueToday", postbackService.getTodayRevenueByBuyer(buyerId));
        response.put("revenueYesterday", postbackService.getYesterdayRevenueByBuyer(buyerId));
        response.put("spentToday", spentService.getSpentByToday(buyerId));
        response.put("spentYesterday", spentService.getSpentByYesterday(buyerId));
        response.put("totalPaid", payrolls.get().parallelStream().mapToDouble(Payroll::getSum).sum());
        response.put("bonus", realProfit > 0 ? realProfit * 0.2 : 0);
        return response;
    }

    @Override
    public Map<String, List<BuyerProfit>> getChartData(int buyerId, String from, String to, String filter) {
        HashMap<String, List<BuyerProfit>> response = new HashMap<>();
        response.put("data", dashboardDao.findChartData(buyerId, from, to, filter));
        return response;
    }
}
