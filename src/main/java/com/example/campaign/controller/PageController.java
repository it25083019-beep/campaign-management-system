package com.example.campaign.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/", ""})
    public String home() {
        return "forward:/admin.html";
    }

    @GetMapping({"/admin", "/admin/"})
    public String admin() {
        return "forward:/admin.html";
    }

    @GetMapping({"/register", "/register/"})
    public String register() {
        return "forward:/register.html";
    }
}