package com.omnia.admin.controller;

import com.omnia.admin.dto.StatisticFilterDto;
import com.omnia.admin.service.StatisticService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("statistic")
public class StatisticController {

    private final StatisticService statisticService;

    @PostMapping
    public ResponseEntity getStatistic(@RequestBody StatisticFilterDto filter) {
        return ResponseEntity.ok(statisticService.getStatistics(filter));
    }
}
