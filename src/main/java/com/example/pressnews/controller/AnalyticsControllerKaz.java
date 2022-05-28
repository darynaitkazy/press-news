package com.example.pressnews.controller;

import com.example.pressnews.model.Analytics;
import com.example.pressnews.model.News;
import com.example.pressnews.service.AnalyticsService;
import com.example.pressnews.service.NewsService;
import com.example.pressnews.service.XMLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class AnalyticsControllerKaz {
    @Value("${uploadDir}")
    private String uploadFolder;

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private XMLService xmlService;

    @GetMapping("/kaz/analytics")
    public String analytics(@RequestParam(defaultValue = "0") Integer page,
                            @RequestParam(required = false, defaultValue = "16") Integer size,
                            @RequestParam(required = false, defaultValue = "createDate") String sortingField,
                            @RequestParam(required = false, defaultValue = "DESC") String sortingDirection,
                            @RequestParam(required = false, defaultValue = "createTime") String sortingFieldTime,
                            Model model) {
        Page<Analytics> analytics = analyticsService.getAllAnalytics(page, size, sortingField, sortingDirection, sortingFieldTime);
        model.addAttribute("analytics", analytics);

        Integer cnt = analyticsService.countAllAnalytics();
        int maxNumberOfPages = 0;
        if (cnt % 16 == 0) maxNumberOfPages = cnt/16;
        else maxNumberOfPages = cnt/16+1;
        model.addAttribute("maxNumberOfPages", maxNumberOfPages);
        model.addAttribute("currentPage", page);

        LocalDate t = LocalDate.now();
        String today = t.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        model.addAttribute("usd", xmlService.parseUSD(today));
        model.addAttribute("eur", xmlService.parseEUR(today));
        model.addAttribute("rub", xmlService.parseRUB(today));

        return "kaz/analytics";
    }


    @GetMapping("/kaz/analytics/{link_name}")
    String showNewsId(@PathVariable("link_name") String link_name, Optional<Analytics> analytics, Model model) {
        try {
            if (link_name != null) {
                analytics = analyticsService.getAnalyticsByLink_name(link_name);
                Analytics analytics1 = new Analytics(
                        analytics.get().getId(),
                        analytics.get().getAuthor_id(),
                        analytics.get().getCreateDate(),
                        analytics.get().getCreateTime(),
                        analytics.get().getImg(),
                        analytics.get().getViews()+1,
                        analytics.get().getTime_to_read(),
                        analytics.get().getLink_name(),
                        analytics.get().getTitle(),
                        analytics.get().getDescription(),
                        analytics.get().getText(),
                        analytics.get().getLink_name_kaz(),
                        analytics.get().getTitle_kaz(),
                        analytics.get().getDescription_kaz(),
                        analytics.get().getText_kaz()
                );
                analyticsService.saveAnalytics(analytics1);
                if (analytics.isPresent()) {
                    model.addAttribute("id", analytics.get().getId());
                    model.addAttribute("author_id", analytics.get().getAuthor_id());
                    model.addAttribute("title", analytics.get().getTitle());
                    model.addAttribute("createDate", analytics.get().getCreateDate());
                    model.addAttribute("createTime", analytics.get().getCreateTime());
                    model.addAttribute("description", analytics.get().getDescription());
                    model.addAttribute("text", analytics.get().getText());
                    model.addAttribute("time_to_read", analytics.get().getTime_to_read());
                    model.addAttribute("link_name", analytics.get().getLink_name());
                    model.addAttribute("views", analytics.get().getViews());
                    model.addAttribute("link_name_kaz", analytics.get().getLink_name_kaz());
                    model.addAttribute("title_kaz", analytics.get().getTitle_kaz());
                    model.addAttribute("description_kaz", analytics.get().getDescription_kaz());
                    model.addAttribute("text_kaz", analytics.get().getText_kaz());
                    model.addAttribute("type", 2);
                    List<News> popularNewsForArticle = newsService.getPopularFourteenNews();
                    model.addAttribute("popularNewsForArticle", popularNewsForArticle);
                    List<News> recentNewsForArticle = newsService.getRecentFourteenNews();
                    model.addAttribute("recentNewsForArticle", recentNewsForArticle);
                    return "kaz/article";
                }
                return "redirect:/";
            }
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }


}
