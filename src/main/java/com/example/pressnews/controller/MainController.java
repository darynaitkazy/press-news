package com.example.pressnews.controller;

import com.example.pressnews.model.Analytics;
import com.example.pressnews.model.News;
import com.example.pressnews.service.AnalyticsService;
import com.example.pressnews.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    @Value("${uploadDir}")
    private String uploadFolder;

    @Autowired
    private NewsService newsService;

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/")
    public String main(Model model) {
        List<News> news = newsService.getPopuparElevenNews();
        List<News> firstList = news.subList(0,1);
        List<News> secondList = news.subList(1, 7);
        List<News> thirdList = news.subList(7, 11);
        model.addAttribute("firstList", firstList);
        model.addAttribute("secondList", secondList);
        model.addAttribute("thirdList", thirdList);

        List<Analytics> analytics = analyticsService.getPopuparSevenAnalytics();
        List<Analytics> firstListAnalytics = analytics.subList(0, 1);
        List<Analytics> secondListAnalytics = analytics.subList(1, 3);
        List<Analytics> thirdListAnalytics = analytics.subList(3, 7);
        model.addAttribute("firstListAnalytics", firstListAnalytics);
        model.addAttribute("secondListAnalytics", secondListAnalytics);
        model.addAttribute("thirdListAnalytics", thirdListAnalytics);
        return "main";
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



}
