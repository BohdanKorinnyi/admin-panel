package com.omnia.admin.service.impl;

import com.omnia.admin.model.Payroll;
import com.omnia.admin.service.DashboardService;
import com.omnia.admin.service.PayrollService;
import com.omnia.admin.service.PostbackService;
import com.omnia.admin.service.StatisticService;
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

    @Override
    public Map<String, Object> getDashboardData(Integer buyerId) throws ExecutionException, InterruptedException {
        HashMap<String, Object> response = new HashMap<>();
        CompletableFuture<Float> revenue = supplyAsync(() -> postbackService.getRevenueByBuyer(buyerId));
        CompletableFuture<List<Payroll>> payrolls = supplyAsync(() -> payrollService.findPayrollsByBuyerId(buyerId));
        CompletableFuture<Float> profit = supplyAsync(() -> statisticService.getProfitByBuyer(buyerId));

        CompletableFuture.allOf(revenue, payrolls, profit);

        response.put("revenue", revenue.get());
        response.put("payroll", payrolls.get());
        return response;
    }
}
