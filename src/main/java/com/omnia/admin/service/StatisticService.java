package com.omnia.admin.service;

import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.statistic.Stats;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

@FunctionalInterface
public interface StatisticService {
    List<Stats> getBuyerStatistics(StatisticFilter filter) throws ExecutionException, InterruptedException;

    default <T> Map<Integer, List<T>> groupByBuyerId(List<T> list, Function<T, Integer> groupingBy) {
        return list.stream().collect(Collectors.groupingBy(groupingBy, Collectors.toList()));
    }
}
