package com.omnia.admin.controller;

import com.omnia.admin.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    @GetMapping("me")
    public ResponseEntity me(HttpServletRequest request) {
        if (request.getUserPrincipal() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) request.getUserPrincipal();
            return ResponseEntity.ok(user.getPrincipal());
        }
        throw new BadCredentialsException("Wrong");
    }

    @GetMapping("password")
    public void changePassword(HttpServletRequest request, @RequestParam String existing,
                               @RequestParam String created, @RequestParam String confirmed) {
        UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) request.getUserPrincipal();
        userService.changePassword(existing, created, confirmed, user.getName());
    }
}
