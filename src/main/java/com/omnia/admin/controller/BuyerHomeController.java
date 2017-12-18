package com.omnia.admin.controller;

import com.omnia.admin.model.Role;
import com.omnia.admin.service.SpentService;
import com.omnia.admin.util.UserPrincipalUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("buyer/home")
public class BuyerHomeController {

    private final SpentService spentService;

    @GetMapping("spent")
    public ResponseEntity getBuyerSpentByPeriod(HttpServletRequest request,
                                                @RequestParam(required = false) String from,
                                                @RequestParam(required = false) String to) {
        if (UserPrincipalUtils.isRole(request, Role.BUYER)) {
            return ResponseEntity.ok(spentService.getSpentByCurrentMonth(UserPrincipalUtils.getBuyerId(request)));
        }
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }
}
