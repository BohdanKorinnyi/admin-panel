package com.omnia.admin.controller;

import com.omnia.admin.dao.StaffDao;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class StaffController {
    private final StaffDao staffDao;

    @GetMapping("staff")
    public ResponseEntity getAllStaff() {
        return ResponseEntity.ok(staffDao.findAll());
    }
}
