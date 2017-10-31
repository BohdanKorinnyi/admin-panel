package com.omnia.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("me")
    public ResponseEntity me(HttpServletRequest request) {
        if (request.getUserPrincipal() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) request.getUserPrincipal();
            return ResponseEntity.ok(user.getPrincipal());
        }
        throw new BadCredentialsException("Wrong");
    }
}
