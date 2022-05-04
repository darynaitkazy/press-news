package com.example.pressnews.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String main() {
        return "main";
    }
    @GetMapping("/allnews")
    public String allnews() {
        return "allnews";
    }
    @GetMapping("/analytics")
    public String analytics() {
        return "analytics";
    }
    @GetMapping("/interview")
    public String interview() {
        return "interview";
    }
    @GetMapping("/opinions")
    public String opinions() {
        return "opinions";
    }
    @GetMapping("/article")
    public String article() {
        return "article";
    }


    @GetMapping("/admin")
    public String adminpanel() {
        return "adminpanel";
    }

    @GetMapping("/admin/add-news")
    public String addnews() {
        return "add-news";
    }

}
