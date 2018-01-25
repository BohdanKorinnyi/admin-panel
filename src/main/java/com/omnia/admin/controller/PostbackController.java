package com.omnia.admin.controller;

import com.google.common.collect.ImmutableMap;
import com.omnia.admin.service.PostbackService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.omnia.admin.util.UserPrincipalUtils.getBuyerId;

@RestController
@AllArgsConstructor
@RequestMapping("postback")
public class PostbackController {

    private final PostbackService postbackService;

    @GetMapping("year/{year}")
    public ResponseEntity getRevenueByYear(HttpServletRequest request, @PathVariable int year) {
        return ResponseEntity.ok(postbackService.getRevenueByBuyerIdAndYear(getBuyerId(request), year));
    }

    @GetMapping
    public ResponseEntity getPostbackByConversionId(@RequestParam Integer conversionId) {
        return ResponseEntity.ok(postbackService.getPostbacksByConversionId(conversionId));
    }

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
        return ResponseEntity.ok(postbackService.getRevenueByBuyer(getBuyerId(request)));
    }
}