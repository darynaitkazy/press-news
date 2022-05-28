package com.example.pressnews.controller;

import com.example.pressnews.model.Analytics;
import com.example.pressnews.model.News;
import com.example.pressnews.service.AnalyticsService;
import com.example.pressnews.service.NewsService;
import com.example.pressnews.service.XMLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class MainControllerKaz {
    @Value("${uploadDir}")
    private String uploadFolder;

    @Autowired
    private NewsService newsService;

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private XMLService xmlService;

    @GetMapping("/kaz/")
    public String main(Model model, @RequestParam(required = false, defaultValue = "0") int type) {
        List<News> news = newsService.getPopularElevenNews();
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

        LocalDate t = LocalDate.now();
        String today = t.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        model.addAttribute("usd", xmlService.parseUSD(today));
        model.addAttribute("eur", xmlService.parseEUR(today));
        model.addAttribute("rub", xmlService.parseRUB(today));

        return "kaz/main";
    }




}
