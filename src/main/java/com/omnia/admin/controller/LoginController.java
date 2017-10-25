package com.omnia.admin.controller;

import com.omnia.admin.dto.LoginDto;
import com.omnia.admin.model.Role;
import com.omnia.admin.security.annotation.RequiredRole;
import com.omnia.admin.security.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("login")
    @RequiredRole(roles = {Role.ADMIN})
    public ResponseEntity login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginDto loginDto) {
        return ResponseEntity.ok().body(loginService.authenticate(response, loginDto));
    }
}
