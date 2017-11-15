package com.omnia.admin.service.impl;

import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.BuyerProjection;
import com.omnia.admin.model.Expenses;
import com.omnia.admin.model.Source;
import com.omnia.admin.model.statistic.ExpensesResult;
import com.omnia.admin.model.statistic.PostbackResult;
import com.omnia.admin.model.statistic.SourcesResult;
import com.omnia.admin.model.statistic.Stats;
import com.omnia.admin.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.omnia.admin.grid.filter.FilterConstant.COMMA;
import static com.omnia.admin.grid.filter.FilterConstant.DOT;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@Log4j
@Service
@AllArgsConstructor
public final class StatisticServiceImpl implements StatisticService {
    private final SourceStatsService sourceStatsService;
    private final PostbackService postbackService;
    private final ExpensesService expensesService;
    private final BuyerService buyerService;

    @Override
    public List<Stats> getBuyerStatistics(StatisticFilter filter) throws ExecutionException, InterruptedException {
        CompletableFuture<List<Expenses>> expensesFuture = supplyAsync(() -> expensesService.getExpenses(filter));
        CompletableFuture<List<Source>> sourcesFuture = supplyAsync(() -> sourceStatsService.getSources(filter));
        CompletableFuture<List<PostbackStats>> postbackFuture = supplyAsync(() -> postbackService.getPostbackStats(filter));
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(expensesFuture, sourcesFuture, postbackFuture);
        completableFuture.get();
        List<Expenses> expenses = expensesFuture.get();
        List<PostbackStats> stats = postbackFuture.get();
        List<Source> sources = sourcesFuture.get();

        long start = System.currentTimeMillis();
        Map<Integer, List<Source>> sourcesByBuyerId = groupByBuyerId(sources, Source::getBuyerId);
        Map<Integer, List<Expenses>> expensesByBuyerId = groupByBuyerId(expenses, Expenses::getBuyerId);
        Map<Integer, List<PostbackStats>> postbackByBuyerId = groupByBuyerId(stats, PostbackStats::getBuyerId);

        Map<Integer, Stats> allStatistic = groupResult(sourcesByBuyerId, expensesByBuyerId, postbackByBuyerId);
        List<Stats> result = new ArrayList<>();
        for (Map.Entry<Integer, Stats> statsEntry : allStatistic.entrySet()) {
            String buyerName = buyerService.getBuyerById(statsEntry.getKey());
            BuyerProjection buyerProjection = new BuyerProjection(statsEntry.getKey(), buyerName);
            Stats value = statsEntry.getValue();
            value.setBuyerInfo(buyerProjection);
            result.add(value);
        }
        log.info("Grouping by buyers executed in " + (System.currentTimeMillis() - start) + "ms");
        start = System.currentTimeMillis();
        calculateSums(result);
        log.info("Calculating sums executed in " + (System.currentTimeMillis() - start) + "ms");
        return result;
    }

    private void calculateSums(List<Stats> stats) {
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        for (Stats stat : stats) {
            ExpensesResult expenses = stat.getExpenses();
            if (nonNull(expenses) && !CollectionUtils.isEmpty(expenses.getData())) {
                List<Expenses> expensesData = expenses.getData();
                double sum = expensesData.stream().mapToDouble(Expenses::getSum).sum();
                expenses.setSum(Double.valueOf(decimalFormat.format(sum).replace(COMMA, DOT)));
            }

            PostbackResult postbacks = stat.getPostbacks();
            if (nonNull(postbacks) && !CollectionUtils.isEmpty(postbacks.getData())) {
                List<PostbackStats> postbackStats = postbacks.getData();
                double sum = postbackStats.stream().mapToDouble(PostbackStats::getAmountInUsd).sum();
                postbacks.setSum(Double.valueOf(decimalFormat.format(sum).replace(COMMA, DOT)));
            }

            SourcesResult sources = stat.getSources();
            if (nonNull(sources) && !CollectionUtils.isEmpty(sources.getData())) {
                List<Source> sourcesData = sources.getData();
                double sum = sourcesData.stream().mapToDouble(Source::getSpent).sum();
                sources.setSum(Double.valueOf(decimalFormat.format(sum).replace(COMMA, DOT)));
            }
        }
    }

    //TODO: need to refactor this method using and create one common method for all lists!
    private Map<Integer, Stats> groupResult(Map<Integer, List<Source>> sources,
                                            Map<Integer, List<Expenses>> expenses,
                                            Map<Integer, List<PostbackStats>> postbacks
    ) {
        Map<Integer, Stats> allStatistic = new HashMap<>();
        for (Map.Entry<Integer, List<Source>> source : sources.entrySet()) {
            log.info("Sources for buyerId: " + source.getKey());
            if (allStatistic.containsKey(source.getKey())) {
                SourcesResult result = allStatistic.get(source.getKey()).getSources();
                if (isNull(result)) {
                    result = new SourcesResult();
                    allStatistic.get(source.getKey()).setSources(result);
                }
                if (CollectionUtils.isEmpty(result.getData())) {
                    result.setData(source.getValue());
                } else {
                    result.getData().addAll(source.getValue());
                }
            } else {
                Stats stats = new Stats();
                SourcesResult result = new SourcesResult();
                result.setData(source.getValue());
                stats.setSources(result);
                allStatistic.put(source.getKey(), stats);
            }
        }
        for (Map.Entry<Integer, List<Expenses>> expense : expenses.entrySet()) {
            log.info("Expenses for buyerId: " + expense.getKey());
            if (allStatistic.containsKey(expense.getKey())) {
                ExpensesResult result = allStatistic.get(expense.getKey()).getExpenses();
                if (isNull(result)) {
                    result = new ExpensesResult();
                    allStatistic.get(expense.getKey()).setExpenses(result);
                }
                if (CollectionUtils.isEmpty(result.getData())) {
                    result.setData(expense.getValue());
                } else {
                    result.getData().addAll(expense.getValue());
                }
            } else {
                Stats stats = new Stats();
                ExpensesResult result = new ExpensesResult();
                result.setData(expense.getValue());
                stats.setExpenses(result);
                allStatistic.put(expense.getKey(), stats);
            }
        }
        for (Map.Entry<Integer, List<PostbackStats>> postback : postbacks.entrySet()) {
            log.info("Postbacks for buyerId: " + postback.getKey());
            if (allStatistic.containsKey(postback.getKey())) {
                PostbackResult result = allStatistic.get(postback.getKey()).getPostbacks();
                if (isNull(result)) {
                    result = new PostbackResult();
                    allStatistic.get(postback.getKey()).setPostbacks(result);
                }
                if (CollectionUtils.isEmpty(result.getData())) {
                    result.setData(postback.getValue());
                } else {
                    result.getData().addAll(postback.getValue());
                }
            } else {
                Stats stats = new Stats();
                PostbackResult result = new PostbackResult();
                result.setData(postback.getValue());
                stats.setPostbacks(result);
                allStatistic.put(postback.getKey(), stats);
            }
        }
        return allStatistic;
    }
}
