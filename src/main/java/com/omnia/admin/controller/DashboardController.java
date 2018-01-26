package com.omnia.admin.controller;

import com.google.common.collect.ImmutableSet;
import com.omnia.admin.model.Role;
import com.omnia.admin.service.DashboardService;
import com.omnia.admin.util.UserPrincipalUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

@RestController
@AllArgsConstructor
@RequestMapping("dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity getData(HttpServletRequest request) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(dashboardService.getDashboardData(UserPrincipalUtils.getBuyerId(request)));
    }

    @GetMapping("charts")
    public ResponseEntity getChartsData(HttpServletRequest request, @RequestParam String from, @RequestParam String to, @RequestParam String filter) {
        if (UserPrincipalUtils.hasRole(request, ImmutableSet.of(Role.BUYER))) {
            return ResponseEntity.ok(dashboardService.getChartData(UserPrincipalUtils.getBuyerId(request), from, to, filter));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
