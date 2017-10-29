package com.omnia.admin.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static java.util.Objects.isNull;

@Controller
public class WebController {
    private static final String INVALID_EMAIL_OR_PASSOWORD_MESSAGE = "Invalid email or password";

    @GetMapping(value = "/login")
    public String getLoginPage(Model model, @RequestParam(value = "error", required = false) String error) {
        model.addAttribute("error", isNull(error) ? "" : INVALID_EMAIL_OR_PASSOWORD_MESSAGE);
        return "login";
    }

    @GetMapping(path = "/")
    @PreAuthorize("isAuthenticated()")
    public String index() {
        return "index";
    }

    //    @GetMapping(path = "/creative-dashboard")
    public String index2() {
        return "index-2";
    }

    @GetMapping(path = "/advertiser")
    public String advertiser() {
        return "advertiser";
    }
}
