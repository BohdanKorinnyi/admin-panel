package com.omnia.admin.controller;

import com.google.common.collect.ImmutableSet;
import com.omnia.admin.model.Role;
import com.omnia.admin.service.AdminDashboardService;
import com.omnia.admin.util.UserPrincipalUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("admin/dashboard")
public class AdminDashboardController {
    private final AdminDashboardService adminDashboardService;

    @GetMapping
    public ResponseEntity getData(HttpServletRequest request, @RequestParam String from, @RequestParam String to) {
        if (UserPrincipalUtils.hasRole(request, ImmutableSet.of(Role.ADMIN, Role.DIRECTOR, Role.CFO, Role.CBO))) {
            return ResponseEntity.ok(adminDashboardService.getData(from, to));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("charts")
    public ResponseEntity getChartsData(HttpServletRequest request, @RequestParam String from, @RequestParam String to, @RequestParam String filter) {
        if (UserPrincipalUtils.hasRole(request, ImmutableSet.of(Role.ADMIN, Role.DIRECTOR, Role.CFO, Role.CBO))) {
            return ResponseEntity.ok(adminDashboardService.getChartData(from, to, filter));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
