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
public class AnalyticsController {
    @Value("${uploadDir}")
    private String uploadFolder;

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private XMLService xmlService;

    @GetMapping("/analytics")
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

        return "analytics";
    }

    @GetMapping("/admin/add-analytics")
    public String addanalytics() {
        return "add-analytics";
    }

    @PostMapping("/admin/add-analytics")
    public @ResponseBody
    ResponseEntity<?> createAnalytics(@RequestParam("author_id") long author_id,
                                 @RequestParam("title") String title, @RequestParam("description") String description,
                                 @RequestParam("text") String txt, @RequestParam("time_to_read") int time_to_read,
                                 @RequestParam("link_name") String link_name,
                                      @RequestParam("title_kaz") String title_kaz,
                                      @RequestParam("description_kaz") String description_kaz,
                                      @RequestParam("text_kaz") String txt_kaz,
                                      @RequestParam("link_name_kaz") String link_name_kaz,
                                 Model model, HttpServletRequest request, final @RequestParam("img") MultipartFile file) {
        try {
            String uploadDirectoryAnalytics = request.getServletContext().getRealPath(uploadFolder);
            String fileName = file.getOriginalFilename();
            String filePath = Paths.get(uploadDirectoryAnalytics, fileName).toString();
            if (fileName == null || fileName.contains("..")) {
                model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence \" + filename");
                return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
            }

            Date createDate = new Date();
            long createTime = createDate.getTime();
            try {
                File dir = new File(uploadDirectoryAnalytics);
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
            Analytics analytics = new Analytics();
            analytics.setAuthor_id(author_id);
            analytics.setViews(0);
            analytics.setTitle(title);
            analytics.setDescription(description);
            analytics.setText(txt);
            analytics.setImg(imageData);
            analytics.setCreateDate(createDate);
            analytics.setCreateTime(createTime);
            analytics.setTime_to_read(time_to_read);
            analytics.setLink_name(link_name);
            analytics.setText_kaz(txt_kaz);
            analytics.setDescription_kaz(description_kaz);
            analytics.setTitle_kaz(title_kaz);
            analytics.setLink_name_kaz(link_name_kaz);
            analyticsService.saveAnalytics(analytics);
                return new ResponseEntity<>("Product Saved With File - " + fileName, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/analytics/display/{id}")
    @ResponseBody
    void showImage(@PathVariable("id") Long id, HttpServletResponse response, Optional<Analytics> analytics)
            throws ServletException, IOException {
        analytics = analyticsService.getAnalyticsById(id);
        response.getOutputStream().write(analytics.get().getImg());
        response.getOutputStream().close();
    }

    @GetMapping("/analytics/{link_name}")
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

    @GetMapping("/admin/analytics/{link_name}/edit")
    public String analyticsEdit(@PathVariable(value = "link_name") String link_name, Model model) {
        Optional<Analytics> analytics = analyticsService.getAnalyticsByLink_name(link_name);
        if (analytics.isPresent()) {
            model.addAttribute("id", analytics.get().getId());
            model.addAttribute("title", analytics.get().getTitle());
            model.addAttribute("description", analytics.get().getDescription());
            model.addAttribute("author_id", analytics.get().getAuthor_id());
            model.addAttribute("views", analytics.get().getViews());
            model.addAttribute("text", analytics.get().getText());
            model.addAttribute("link_name", analytics.get().getLink_name());
            model.addAttribute("title_kaz", analytics.get().getTitle_kaz());
            model.addAttribute("description_kaz", analytics.get().getDescription_kaz());
            model.addAttribute("text_kaz", analytics.get().getText_kaz());
            model.addAttribute("time_to_read", analytics.get().getTime_to_read());
            model.addAttribute("link_name_kaz", analytics.get().getLink_name_kaz());
        }
        else {
            return "redirect:/admin";
        }
        return "edit-analytics";
    }

    @PostMapping("/admin/analytics/{link_name}/edit")
    public String analyticsEditPost(@PathVariable(value = "link_name") String link_name, @RequestParam("author_id") long author_id,
                               @RequestParam("title") String title, @RequestParam("description") String description,
                               @RequestParam("text") String txt,
                               @RequestParam("title_kaz") String title_kaz,
                               @RequestParam("description_kaz") String description_kaz,
                               @RequestParam("text_kaz") String txt_kaz,
                               @RequestParam("time_to_read") Integer time_to_read,
                               @RequestParam("link_name_kaz") String link_name_kaz,
                               Model model, final @RequestParam("img") MultipartFile file) throws IOException {
        Analytics analytics = analyticsService.getAnalyticsByLink_name(link_name).orElseThrow();
        analytics.setTitle(title);
        analytics.setViews(analytics.getViews());
        analytics.setLink_name(link_name);
        analytics.setText(txt);
        analytics.setDescription(description);
        analytics.setDescription_kaz(description_kaz);
        analytics.setTitle_kaz(title_kaz);
        analytics.setText_kaz(txt_kaz);
        analytics.setTime_to_read(time_to_read);
        analytics.setCreateTime(analytics.getCreateTime());
        analytics.setCreateDate(analytics.getCreateDate());
        analytics.setAuthor_id(analytics.getAuthor_id());
        analytics.setLink_name_kaz(link_name_kaz);
        if (!file.isEmpty()) {
            byte[] imageData = file.getBytes();
            analytics.setImg(imageData);
        }
        else {
            analytics.setImg(analytics.getImg());
        }
        analyticsService.saveAnalytics(analytics);
        return "redirect:/admin/analytics";
    }

    @PostMapping("/admin/analytics/{link_name}/delete")
    public String analyticsDelete(@PathVariable(value="link_name") String link_name) throws IOException {
        Analytics analytics = analyticsService.getAnalyticsByLink_name(link_name).orElseThrow();
        analyticsService.deleteById(analytics.getId());
        return "redirect:/admin";
    }

}
