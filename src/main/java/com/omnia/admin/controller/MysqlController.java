package com.omnia.admin.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping("time")
public class MysqlController {
    private final JdbcTemplate jdbcTemplate;

    @GetMapping("mysql")
    public ResponseEntity getMysqlTime() {
        return ResponseEntity.ok(jdbcTemplate.queryForObject("SELECT DATE(NOW()) FROM users LIMIT 1;", String.class));
    }

    @GetMapping("server")
    public ResponseEntity getServerTime() {
        return ResponseEntity.ok(LocalDate.now());
    }
}
