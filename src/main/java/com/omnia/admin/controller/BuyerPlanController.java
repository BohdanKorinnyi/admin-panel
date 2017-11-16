package com.omnia.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BuyerPlanController {
    @GetMapping
    public ResponseEntity getPlan(@RequestParam List<String> month, @RequestParam List<String> buyers) {

        return ResponseEntity.ok().build();
    }
}