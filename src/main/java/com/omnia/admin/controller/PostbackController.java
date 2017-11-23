package com.omnia.admin.controller;

import com.google.common.collect.ImmutableMap;
import com.omnia.admin.model.CurrentUser;
import com.omnia.admin.service.PostbackService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("postback")
public class PostbackController {

    private final PostbackService postbackService;

    @GetMapping("fullurl")
    public ResponseEntity getFullUrl(@RequestParam("id") Long postbackId) {
        Optional<String> fullUrl = postbackService.getFullUrl(postbackId);
        if (fullUrl.isPresent()) {
            return ResponseEntity.ok(ImmutableMap.of("fullurl", fullUrl.get()));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("buyers/revenue")
    public ResponseEntity getBuyerRevenue(HttpServletRequest request) {
        if (request.getUserPrincipal() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken userPrincipal = (UsernamePasswordAuthenticationToken) request.getUserPrincipal();
            CurrentUser currentUser = (CurrentUser) userPrincipal.getPrincipal();
            return ResponseEntity.ok(postbackService.getRevenueByBuyer(currentUser.getBuyerId()));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }
}
