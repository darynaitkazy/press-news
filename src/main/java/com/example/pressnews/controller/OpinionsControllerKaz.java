package com.example.pressnews.controller;

import com.example.pressnews.model.News;
import com.example.pressnews.model.Opinions;
import com.example.pressnews.service.NewsService;
import com.example.pressnews.service.OpinionsService;
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
public class OpinionsControllerKaz {
    @Value("${uploadDir}")
    private String uploadFolder;

    @Autowired
    private OpinionsService opinionsService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private XMLService xmlService;

    @GetMapping("/kaz/opinions")
    public String getOpinions(@RequestParam(defaultValue = "0") Integer page,
                          @RequestParam(required = false, defaultValue = "16") Integer size,
                          @RequestParam(required = false, defaultValue = "createDate") String sortingField,
                          @RequestParam(required = false, defaultValue = "DESC") String sortingDirection,
                          @RequestParam(required = false, defaultValue = "createTime") String sortingFieldTime,
                          Model model) {

        Page<Opinions> opinions = opinionsService.getAllOpinions(page, size, sortingField, sortingDirection, sortingFieldTime);
        model.addAttribute("opinions", opinions);
        Integer cnt = opinionsService.countAllOpinions();
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

        return "kaz/opinions";
    }


    @GetMapping("/kaz/opinions/{link_name}")
    String showOpinionsId(@PathVariable("link_name") String link_name,Optional<Opinions> opinions, Model model) {
        try {
            if (link_name != null) {
                opinions = opinionsService.getOpinionsByLink_name(link_name);
                Opinions opinions1 = new Opinions(
                        opinions.get().getId(),
                        opinions.get().getAuthor_id(),
                        opinions.get().getCreateDate(),
                        opinions.get().getCreateTime(),
                        opinions.get().getImg(),
                        opinions.get().getViews()+1,
                        opinions.get().getLink_name(),
                        opinions.get().getAuthor(),
                        opinions.get().getAuthor_position(),
                        opinions.get().getTitle(),
                        opinions.get().getText(),
                        opinions.get().getAuthor_position_kaz(),
                        opinions.get().getTitle_kaz(),
                        opinions.get().getText_kaz(),
                        opinions.get().getLink_name_kaz()
                );
                opinionsService.saveOpinions(opinions1);
                if (opinions.isPresent()) {
                    model.addAttribute("id", opinions.get().getId());
                    model.addAttribute("author_id", opinions.get().getAuthor_id());
                    model.addAttribute("author", opinions.get().getAuthor());
                    model.addAttribute("author_position", opinions.get().getAuthor_position());
                    model.addAttribute("createDate", opinions.get().getCreateDate());
                    model.addAttribute("createTime", opinions.get().getCreateTime());
                    model.addAttribute("title", opinions.get().getTitle());
                    model.addAttribute("text", opinions.get().getText());
                    model.addAttribute("link_name", opinions.get().getLink_name());
                    model.addAttribute("views", opinions.get().getViews());
                    model.addAttribute("author_position_kaz", opinions.get().getAuthor_position_kaz());
                    model.addAttribute("title_kaz", opinions.get().getTitle_kaz());
                    model.addAttribute("text_kaz", opinions.get().getText_kaz());
                    model.addAttribute("link_name_kaz", opinions.get().getLink_name_kaz());
                    model.addAttribute("type", 4);
                    List<News> popularNewsForArticle = newsService.getPopularFourteenNews();
                    model.addAttribute("popularNewsForArticle", popularNewsForArticle);
                    List<News> recentNewsForArticle = newsService.getRecentFourteenNews();
                    model.addAttribute("recentNewsForArticle", recentNewsForArticle);
                    return "article";
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
