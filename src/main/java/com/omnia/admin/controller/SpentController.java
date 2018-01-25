package com.omnia.admin.controller;

import com.omnia.admin.service.SpentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.omnia.admin.util.UserPrincipalUtils.getBuyerId;

@RestController
@AllArgsConstructor
@RequestMapping("spent")
public class SpentController {
    private final SpentService spentService;

    @GetMapping("year/{year}")
    public ResponseEntity getSpentByBuyerAndYear(HttpServletRequest request, @PathVariable int year) {
        return ResponseEntity.ok(spentService.getSpentByBuyerAndYear(getBuyerId(request), year));
    }
}
