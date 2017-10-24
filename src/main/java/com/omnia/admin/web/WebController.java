package com.omnia.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    //TODO: server works without UI
//    @GetMapping(path = "/")
    public String index() {
        return "index";
    }

//    @GetMapping(path = "/creative-dashboard")
    public String index2() {
        return "index-2";
    }

//    @GetMapping(path = "/advertiser")
    public String advertiser() {
        return "advertiser";
    }
}
