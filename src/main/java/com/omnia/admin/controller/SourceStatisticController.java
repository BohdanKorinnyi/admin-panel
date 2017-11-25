package com.omnia.admin.controller;

import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.service.SourceStatsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("statistic")
public class SourceStatisticController {
    private final SourceStatsService sourceStatsService;

    @GetMapping("buyers")
    public ResponseEntity getSourceStat(@RequestParam(required = false) List<Integer> buyerIds,
                                        @RequestParam(required = false) String from,
                                        @RequestParam(required = false) String to
    ) {
        return ResponseEntity.ok(sourceStatsService.getSourceStat(buyerIds, from, to));
    }

    @PostMapping("old")
    public ResponseEntity getStatistic(@RequestBody StatisticFilter filter) {
        return ResponseEntity.ok(sourceStatsService.getStatistics(filter));
    }

    @PostMapping("daily")
    public ResponseEntity getDailyStatistic(@RequestBody StatisticFilter filter) {
        return ResponseEntity.ok(sourceStatsService.getDailyStatistics(filter));
    }

    @PostMapping("all")
    public ResponseEntity getAllStatistic(@RequestBody StatisticFilter filter) {
        return ResponseEntity.ok(sourceStatsService.getDailyAndGeneralStatistics(filter));
    }
}
