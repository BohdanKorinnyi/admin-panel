package com.omnia.admin.controller;

import com.omnia.admin.service.AdminDashboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("admin/dashboard")
public class AdminDashboardController {
    private final AdminDashboardService adminDashboardService;

    @GetMapping
    @PreAuthorize("hasAnyRole('TEAM_LEADER','CBO','MENTOR','CFO','DIRECTOR','ADMIN')")
    public ResponseEntity getData(@RequestParam String from, @RequestParam String to) {
        return ResponseEntity.ok(adminDashboardService.getData(from, to));
    }
}
