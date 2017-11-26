package com.omnia.admin.controller;

import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.Role;
import com.omnia.admin.service.SourceStatsService;
import com.omnia.admin.util.UserPrincipalUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("statistic")
public class SourceStatisticController {
    private final SourceStatsService sourceStatsService;

    @GetMapping("date")
    public ResponseEntity getSourceStatByBuyer(@RequestParam Integer buyerId, @RequestParam String date) {
        return ResponseEntity.ok(sourceStatsService.getSourceStatByDate(buyerId, date));
    }

    @GetMapping("buyers")
    public ResponseEntity getSourceStat(HttpServletRequest request,
                                        @RequestParam(required = false) List<Integer> buyerIds,
                                        @RequestParam(required = false) String from,
                                        @RequestParam(required = false) String to
    ) {
        if (UserPrincipalUtils.isRole(request, Role.BUYER)) {
            return ResponseEntity.ok(sourceStatsService.getSourceStat(Collections.singletonList(UserPrincipalUtils.getBuyerId(request)), from, to));
        }
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
