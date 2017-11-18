package com.omnia.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("buyer")
public class BuyerPlanController {
    @GetMapping("plan")
    public ResponseEntity getPlan(@RequestParam List<String> month, @RequestParam List<String> buyers) {

        return ResponseEntity.ok().build();
    }
}