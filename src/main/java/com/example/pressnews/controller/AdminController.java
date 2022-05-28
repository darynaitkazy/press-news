package com.example.pressnews.controller;

import com.example.pressnews.model.Analytics;
import com.example.pressnews.model.Interviews;
import com.example.pressnews.model.News;
import com.example.pressnews.model.Opinions;
import com.example.pressnews.service.AnalyticsService;
import com.example.pressnews.service.InterviewService;
import com.example.pressnews.service.NewsService;
import com.example.pressnews.service.OpinionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
    @Autowired
    private NewsService newsService;

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private InterviewService interviewService;

    @Autowired
    private OpinionsService opinionsService;

    @GetMapping("/admin")
    public String adminpanel() {
        return "adminpanel";
    }

    @GetMapping("/admin/news")
    public String allnews(@RequestParam(required = false, defaultValue = "0") Integer page,
                          @RequestParam(required = false, defaultValue = "10") Integer size,
                          @RequestParam(required = false, defaultValue = "createDate") String sortingField,
                          @RequestParam(required = false, defaultValue = "DESC") String sortingDirection,
                          @RequestParam(required = false, defaultValue = "createTime") String sortingFieldTime,
                          Model model) {
        Page<News> news = newsService.getAllNews(page,size,sortingField,sortingDirection, sortingFieldTime);
        model.addAttribute("news", news);
        Integer cnt = newsService.countAllNews();
        int maxNumberOfPages = 0;
        if (cnt % 10 == 0) maxNumberOfPages = cnt/10;
        else maxNumberOfPages = cnt/10+1;

        model.addAttribute("maxNumberOfPages", maxNumberOfPages);
        model.addAttribute("currentPage", page);
        return "admin-news";
    }

    @GetMapping("/admin/analytics")
    public String allanalytics(@RequestParam(required = false, defaultValue = "0") Integer page,
                          @RequestParam(required = false, defaultValue = "16") Integer size,
                          @RequestParam(required = false, defaultValue = "createDate") String sortingField,
                          @RequestParam(required = false, defaultValue = "DESC") String sortingDirection,
                          @RequestParam(required = false, defaultValue = "createTime") String sortingFieldTime,
                          Model model) {
        Integer cnt = analyticsService.countAllAnalytics();
        int maxNumberOfPages = 0;
        if (cnt % 16 == 0) maxNumberOfPages = cnt/16;
        else maxNumberOfPages = cnt/16+1;
        model.addAttribute("maxNumberOfPages", maxNumberOfPages);
        model.addAttribute("currentPage", page);
        Page<Analytics> analytics = analyticsService.getAllAnalytics(page,size,sortingField,sortingDirection, sortingFieldTime);
        model.addAttribute("analytics", analytics);
        return "admin-analytics";
    }

    @GetMapping("/admin/interviews")
    public String allinterviews(@RequestParam(required = false, defaultValue = "0") Integer page,
                               @RequestParam(required = false, defaultValue = "16") Integer size,
                               @RequestParam(required = false, defaultValue = "createDate") String sortingField,
                               @RequestParam(required = false, defaultValue = "DESC") String sortingDirection,
                               @RequestParam(required = false, defaultValue = "createTime") String sortingFieldTime,
                               Model model) {
        Integer cnt = interviewService.countAllInterviews();
        int maxNumberOfPages = 0;
        if (cnt % 16 == 0) maxNumberOfPages = cnt/16;
        else maxNumberOfPages = cnt/16+1;
        model.addAttribute("maxNumberOfPages", maxNumberOfPages);
        model.addAttribute("currentPage", page);
        Page<Interviews> interviews = interviewService.getAllInterviews(page,size,sortingField,sortingDirection, sortingFieldTime);
        model.addAttribute("interviews", interviews);
        return "admin-interview";
    }
    @GetMapping("/admin/opinions")
    public String allOpinions(@RequestParam(required = false, defaultValue = "0") Integer page,
                                @RequestParam(required = false, defaultValue = "16") Integer size,
                                @RequestParam(required = false, defaultValue = "createDate") String sortingField,
                                @RequestParam(required = false, defaultValue = "DESC") String sortingDirection,
                                @RequestParam(required = false, defaultValue = "createTime") String sortingFieldTime,
                                Model model) {
        Integer cnt = opinionsService.countAllOpinions();
        int maxNumberOfPages = 0;
        if (cnt % 16 == 0) maxNumberOfPages = cnt/16;
        else maxNumberOfPages = cnt/16+1;
        model.addAttribute("maxNumberOfPages", maxNumberOfPages);
        model.addAttribute("currentPage", page);
        Page<Opinions> opinions = opinionsService.getAllOpinions(page,size,sortingField,sortingDirection, sortingFieldTime);
        model.addAttribute("opinions", opinions);
        return "admin-opinions";
    }

    @GetMapping("/login")
    public String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) return "admin";
        else return "login";
    }
}
