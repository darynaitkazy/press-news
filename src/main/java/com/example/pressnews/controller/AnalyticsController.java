package com.example.pressnews.controller;

import com.example.pressnews.model.Analytics;
import com.example.pressnews.model.News;
import com.example.pressnews.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class AnalyticsController {
    @Value("${uploadDir}")
    private String uploadFolder;

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/analytics")
    public String analytics(Model modelAnalytics) {
        List<Analytics> analytics = analyticsService.getAllAnalytics();
        modelAnalytics.addAttribute("analytics", analytics);
        return "analytics";
    }

    @GetMapping("/admin/add-analytics")
    public String addanalytics() {
        return "add-analytics";
    }

    @PostMapping("admin/add-analytics")
    public @ResponseBody
    ResponseEntity<?> createAnalytics(@RequestParam("author_id") long author_id,
                                 @RequestParam("title") String title, @RequestParam("description") String description,
                                 @RequestParam("text") String txt, @RequestParam("time_to_read") int time_to_read,
                                 @RequestParam("link_name") String link_name,
                                 Model model, HttpServletRequest request, final @RequestParam("img") MultipartFile file) {
        try {
            String uploadDirectoryAnalytics = request.getServletContext().getRealPath(uploadFolder);
            String fileName = file.getOriginalFilename();
            String filePath = Paths.get(uploadDirectoryAnalytics, fileName).toString();
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
            analytics.setTitle(titles[0]);
            analytics.setDescription(descriptions[0]);
            analytics.setText(txts[0]);
            analytics.setImg(imageData);
            analytics.setCreateDate(createDate);
            analytics.setCreateTime(createTime);
            analytics.setTime_to_read(time_to_read);
            analytics.setLink_name(link_names[0]);
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
                        analytics.get().getTitle(),
                        analytics.get().getDescription(),
                        analytics.get().getText(),
                        analytics.get().getCreateDate(),
                        analytics.get().getCreateTime(),
                        analytics.get().getImg(),
                        analytics.get().getViews()+1,
                        analytics.get().getTime_to_read(),
                        analytics.get().getLink_name()
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
