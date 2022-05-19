package com.example.pressnews.controller;

import com.example.pressnews.model.News;
import com.example.pressnews.repos.NewsRepository;
import com.example.pressnews.service.NewsService;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class NewsController {
    @Value("${uploadDir}")
    private String uploadFolder;

    @Autowired
    private NewsService newsService;

    private int pageSize = 10;

    @GetMapping("/news")
    public String allnews(Model model) {
        Page<News> news = newsService.getAllNews(pageSize);
        model.addAttribute("news", news);
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
                                 Model model, HttpServletRequest request, final @RequestParam("img") MultipartFile file) {
        try {
            String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
            String fileName = file.getOriginalFilename();
            String filePath = Paths.get(uploadDirectory, fileName).toString();
            if (fileName == null || fileName.contains("..")) {
                model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence \" + filename");
                return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
            }
            String[] titles = title.split(",");
            String[] descriptions = description.split(",");
            String[] txts = txt.split(",");
            String[] link_names = link_name.split(",");
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
            news.setTitle(titles[0]);
            news.setDescription(descriptions[0]);
            news.setText(txts[0]);
            news.setLink_name(link_names[0]);
            news.setImg(imageData);
            news.setCreateDate(createDate);
            news.setCreateTime(createTime);
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

    @GetMapping("/{link_name}")
    String showNewsId(@PathVariable("link_name") String link_name,Optional<News> news, Model model) {
        try {
            if (link_name != null) {
                news = newsService.getNewsByLink_name(link_name);
                News news1 = new News(
                        news.get().getId(),
                        news.get().getAuthor_id(),
                        news.get().getTitle(),
                        news.get().getDescription(),
                        news.get().getText(),
                        news.get().getCreateDate(),
                        news.get().getCreateTime(),
                        news.get().getImg(),
                        news.get().getViews()+1,
                        news.get().getLink_name()
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
