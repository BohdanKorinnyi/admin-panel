package com.omnia.admin.controller;

import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.service.StatisticService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("statistic")
@AllArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;

    @PostMapping
    public ResponseEntity getStatistic(@RequestBody StatisticFilter filter) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(statisticService.getBuyerStatistics(filter));
    }
}
