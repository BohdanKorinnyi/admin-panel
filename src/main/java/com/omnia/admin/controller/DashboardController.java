package com.omnia.admin.controller;

import com.omnia.admin.model.CurrentUser;
import com.omnia.admin.service.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("dashboard")
@AllArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("get")
    public ResponseEntity getData(HttpServletRequest request) throws ExecutionException, InterruptedException {
        if (request.getUserPrincipal() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken userPrincipal = (UsernamePasswordAuthenticationToken) request.getUserPrincipal();
            CurrentUser currentUser = (CurrentUser) userPrincipal.getPrincipal();
            return ResponseEntity.ok(dashboardService.getDashboardData(currentUser.getBuyerId()));
        }
        return ResponseEntity.ok().build();
    }
}
