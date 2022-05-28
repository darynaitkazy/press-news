package com.example.pressnews.controller;

import com.example.pressnews.model.News;
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
public class NewsControllerKaz {
    @Value("${uploadDir}")
    private String uploadFolder;

    @Autowired
    private NewsService newsService;

    @Autowired
    private XMLService xmlService;

    @GetMapping("/kaz/news")
    public String getNews(@RequestParam(defaultValue = "0") Integer page,
                          @RequestParam(required = false, defaultValue = "10") Integer size,
                          @RequestParam(required = false, defaultValue = "createDate") String sortingField,
                          @RequestParam(required = false, defaultValue = "DESC") String sortingDirection,
                          @RequestParam(required = false, defaultValue = "createTime") String sortingFieldTime,
                          @RequestParam(required = false) String keyword,
                          Model model) {

        if (keyword == null || keyword.length() == 0) {
            Page<News> news = newsService.getAllNews(page, size, sortingField, sortingDirection, sortingFieldTime);
            model.addAttribute("news", news);
            Integer cnt = newsService.countAllNews();
            int maxNumberOfPages = 0;
            if (cnt % 10 == 0) maxNumberOfPages = cnt/10;
            else maxNumberOfPages = cnt/10+1;

            model.addAttribute("maxNumberOfPages", maxNumberOfPages);
        }
        else {
            List<News> news = newsService.findByKeyword(keyword);
            model.addAttribute("news", news);
            model.addAttribute("keyword", keyword);
            model.addAttribute("keywordSize", keyword.length());
        }
        model.addAttribute("currentPage", page);

        LocalDate t = LocalDate.now();
        String today = t.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        model.addAttribute("usd", xmlService.parseUSD(today));
        model.addAttribute("eur", xmlService.parseEUR(today));
        model.addAttribute("rub", xmlService.parseRUB(today));

        return "kaz/allnews";
    }


    @GetMapping("/kaz/news/display/{id}")
    @ResponseBody
    void showImage(@PathVariable("id") Long id, HttpServletResponse response, Optional<News> news)
            throws ServletException, IOException {
        news = newsService.getNewsById(id);
        response.getOutputStream().write(news.get().getImg());
        response.getOutputStream().close();
    }

    @GetMapping("/kaz/news/{link_name}")
    String showNewsId(@PathVariable("link_name") String link_name,Optional<News> news, Model model) {
        try {
            if (link_name != null) {
                news = newsService.getNewsByLink_name(link_name);
                News news1 = new News(
                        news.get().getId(),
                        news.get().getAuthor_id(),
                        news.get().getCreateDate(),
                        news.get().getCreateTime(),
                        news.get().getImg(),
                        news.get().getViews()+1,
                        news.get().getLink_name(),
                        news.get().getTitle(),
                        news.get().getDescription(),
                        news.get().getText(),
                        news.get().getTitle_kaz(),
                        news.get().getDescription_kaz(),
                        news.get().getText_kaz(),
                        news.get().getLink_name_kaz()
                );
                newsService.saveNews(news1);
                if (news.isPresent()) {
                    model.addAttribute("id", news.get().getId());
                    model.addAttribute("author_id", news.get().getAuthor_id());
                    model.addAttribute("title", news.get().getTitle());
                    model.addAttribute("createDate", news.get().getCreateDate());
                    model.addAttribute("createTime", news.get().getCreateTime());
                    model.addAttribute("description", news.get().getDescription());
                    model.addAttribute("text", news.get().getText());
                    model.addAttribute("views", news.get().getViews());
                    model.addAttribute("link_name", news.get().getLink_name());
                    model.addAttribute("type", 1);
                    model.addAttribute("title_kaz", news.get().getTitle_kaz());
                    model.addAttribute("description_kaz", news.get().getDescription_kaz());
                    model.addAttribute("text_kaz", news.get().getText_kaz());
                    model.addAttribute("link_name_kaz", news.get().getLink_name_kaz());
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
