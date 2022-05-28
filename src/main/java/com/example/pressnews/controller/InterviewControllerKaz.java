package com.example.pressnews.controller;

import com.example.pressnews.model.Interviews;
import com.example.pressnews.model.News;
import com.example.pressnews.service.InterviewService;
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
public class InterviewControllerKaz {
    @Value("${uploadDir}")
    private String uploadFolder;

    @Autowired
    private InterviewService interviewService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private XMLService xmlService;

    @GetMapping("/kaz/interviews")
    public String interviews(@RequestParam(defaultValue = "0") Integer page,
                             @RequestParam(required = false, defaultValue = "16") Integer size,
                             @RequestParam(required = false, defaultValue = "createDate") String sortingField,
                             @RequestParam(required = false, defaultValue = "DESC") String sortingDirection,
                             @RequestParam(required = false, defaultValue = "createTime") String sortingFieldTime,
                             Model model) {
        Page<Interviews> interviews = interviewService.getAllInterviews(page, size, sortingField, sortingDirection, sortingFieldTime);
        model.addAttribute("interviews", interviews);

        Integer cnt = interviewService.countAllInterviews();
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

        return "kaz/interview";
    }

    @GetMapping("/kaz/interviews/{link_name}")
    String showInterviewsId(@PathVariable("link_name") String link_name, Optional<Interviews> interviews, Model model) {
        try {
            if (link_name != null) {
                interviews = interviewService.getInterviewsByLink_name(link_name);
                Interviews interviews1 = new Interviews(
                        interviews.get().getId(),
                        interviews.get().getAuthor_id(),
                        interviews.get().getCreateDate(),
                        interviews.get().getCreateTime(),
                        interviews.get().getImg(),
                        interviews.get().getViews()+1,
                        interviews.get().getTime_to_read(),
                        interviews.get().getLink_name(),
                        interviews.get().getTitle(),
                        interviews.get().getDescription(),
                        interviews.get().getText(),
                        interviews.get().getLink_name_kaz(),
                        interviews.get().getTitle_kaz(),
                        interviews.get().getDescription_kaz(),
                        interviews.get().getText_kaz()
                );
                interviewService.saveInterview(interviews1);
                if (interviews.isPresent()) {
                    model.addAttribute("id", interviews.get().getId());
                    model.addAttribute("author_id", interviews.get().getAuthor_id());
                    model.addAttribute("title", interviews.get().getTitle());
                    model.addAttribute("createDate", interviews.get().getCreateDate());
                    model.addAttribute("createTime", interviews.get().getCreateTime());
                    model.addAttribute("description", interviews.get().getDescription());
                    model.addAttribute("text", interviews.get().getText());
                    model.addAttribute("time_to_read", interviews.get().getTime_to_read());
                    model.addAttribute("link_name", interviews.get().getLink_name());
                    model.addAttribute("views", interviews.get().getViews());
                    model.addAttribute("link_name_kaz", interviews.get().getLink_name_kaz());
                    model.addAttribute("title_kaz", interviews.get().getTitle_kaz());
                    model.addAttribute("description_kaz", interviews.get().getDescription_kaz());
                    model.addAttribute("text_kaz", interviews.get().getText_kaz());
                    model.addAttribute("type", 3);
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
