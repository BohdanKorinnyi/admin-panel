package com.omnia.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("report/advertiser")
public class AdvertiserReportController {
    @GetMapping
    public ResponseEntity getData() {
        return ResponseEntity.ok().build();
    }
}
