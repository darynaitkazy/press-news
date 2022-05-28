package com.example.pressnews.controller;

import com.example.pressnews.model.News;
import com.example.pressnews.repos.NewsRepository;
import com.example.pressnews.service.NewsService;
import com.example.pressnews.service.XMLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class NewsController {
    @Value("${uploadDir}")
    private String uploadFolder;

    @Autowired
    private NewsService newsService;

    @Autowired
    private XMLService xmlService;

    @GetMapping("/news")
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

        return "allnews";
    }


    @GetMapping("/admin/add-news")
    public String addnews() {
        return "add-news";
    }

    @PostMapping("admin/add-news")
    public @ResponseBody
    ResponseEntity<?> createNews(@RequestParam("author_id") long author_id,
                                 @RequestParam("title") String title, @RequestParam("description") String description,
                                 @RequestParam("text") String txt,
                                 @RequestParam("link_name") String link_name,
                                 @RequestParam("link_name_kaz") String link_name_kaz,
                                 @RequestParam("title_kaz") String title_kaz,
                                 @RequestParam("description_kaz") String description_kaz,
                                 @RequestParam("text_kaz") String txt_kaz,
                                 Model model, HttpServletRequest request, final @RequestParam("img") MultipartFile file) {
        try {
            String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
            String fileName = file.getOriginalFilename();
            String filePath = Paths.get(uploadDirectory, fileName).toString();
            if (fileName == null || fileName.contains("..")) {
                model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence \" + filename");
                return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
            }
            Date createDate = new Date();
            long createTime = createDate.getTime();
            try {
                File dir = new File(uploadDirectory);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                stream.write(file.getBytes());
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            byte[] imageData = file.getBytes();
            News news = new News();
            news.setAuthor_id(author_id);
            news.setViews(0);
            news.setTitle(title);
            news.setDescription(description);
            news.setText(txt);
            news.setLink_name(link_name);
            news.setImg(imageData);
            news.setCreateDate(createDate);
            news.setCreateTime(createTime);
            news.setText_kaz(txt_kaz);
            news.setTitle_kaz(title_kaz);
            news.setDescription_kaz(description_kaz);
            news.setLink_name_kaz(link_name_kaz);
            newsService.saveNews(news);
            return new ResponseEntity<>("Product Saved With File - " + fileName, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/news/display/{id}")
    @ResponseBody
    void showImage(@PathVariable("id") Long id, HttpServletResponse response, Optional<News> news)
            throws ServletException, IOException {
        news = newsService.getNewsById(id);
        response.getOutputStream().write(news.get().getImg());
        response.getOutputStream().close();
    }

    @GetMapping("news/{link_name}")
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

    @GetMapping("/admin/news/{link_name}/edit")
    public String newsEdit(@PathVariable(value = "link_name") String link_name, Model model) {
        Optional<News> news = newsService.getNewsByLink_name(link_name);
        if (news.isPresent()) {
            model.addAttribute("id", news.get().getId());
            model.addAttribute("title", news.get().getTitle());
            model.addAttribute("description", news.get().getDescription());
            model.addAttribute("author_id", news.get().getAuthor_id());
            model.addAttribute("views", news.get().getViews());
            model.addAttribute("text", news.get().getText());
            model.addAttribute("link_name", news.get().getLink_name());
            model.addAttribute("title_kaz", news.get().getTitle_kaz());
            model.addAttribute("description_kaz", news.get().getDescription_kaz());
            model.addAttribute("text_kaz", news.get().getText_kaz());
            model.addAttribute("link_name_kaz", news.get().getLink_name_kaz());
        }
        else {
            return "redirect:/admin";
        }
        return "edit-news";
    }

    @PostMapping("/admin/news/{link_name}/edit")
    public String newsEditPost(@PathVariable(value = "link_name") String link_name, @RequestParam("author_id") long author_id,
                               @RequestParam("title") String title, @RequestParam("description") String description,
                               @RequestParam("text") String txt,
                               @RequestParam("title_kaz") String title_kaz,
                               @RequestParam("description_kaz") String description_kaz,
                               @RequestParam("text_kaz") String txt_kaz,
                               @RequestParam("link_name_kaz") String link_name_kaz,
                               Model model, final @RequestParam("img") MultipartFile file) throws IOException {
        News news = newsService.getNewsByLink_name(link_name).orElseThrow();
        news.setTitle(title);
        news.setViews(news.getViews());
        news.setLink_name(link_name);
        news.setText(txt);
        news.setDescription(description);
        news.setDescription_kaz(description_kaz);
        news.setTitle_kaz(title_kaz);
        news.setText_kaz(txt_kaz);
        news.setCreateTime(news.getCreateTime());
        news.setCreateDate(news.getCreateDate());
        news.setAuthor_id(news.getAuthor_id());
        news.setLink_name_kaz(link_name_kaz);

        if (!file.isEmpty()) {
            byte[] imageData = file.getBytes();
            news.setImg(imageData);
        }
        else {
            news.setImg(news.getImg());
        }
        newsService.saveNews(news);
        return "redirect:/admin/news";
    }

    @PostMapping("/admin/news/{link_name}/delete")
    public String newsDelete(@PathVariable(value="link_name") String link_name) throws IOException {
        News news = newsService.getNewsByLink_name(link_name).orElseThrow();
        newsService.deleteById(news.getId());
        return "redirect:/admin";
    }
}
