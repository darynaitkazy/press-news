package com.example.pressnews.controller;

import com.example.pressnews.model.News;
import com.example.pressnews.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
    @Value("${uploadDir}")
    private String uploadFolder;

    @Autowired
    private NewsService newsService;



    @GetMapping("/")
    public String main(Model model, @RequestParam(required = false, defaultValue = "0") int type) {
        List<News> news = newsService.getPopularElevenNews();
        List<News> firstList = news.subList(0,1);
        List<News> secondList = news.subList(1, 7);
        List<News> thirdList = news.subList(7, 11);
        model.addAttribute("firstList", firstList);
        model.addAttribute("secondList", secondList);
        model.addAttribute("thirdList", thirdList);
        Boolean checkActivity = false;
        model.addAttribute("checkActivity", checkActivity);

        List<News> listCurrent;

        if(type == 0){
            listCurrent = newsService.getRecentFourteenNews();
        } else {
            listCurrent = newsService.getPopularFourteenNews();
        }
        model.addAttribute("list", listCurrent);
        model.addAttribute("type", type);

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
