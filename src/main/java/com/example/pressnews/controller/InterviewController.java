package com.example.pressnews.controller;

import com.example.pressnews.model.Analytics;
import com.example.pressnews.model.Interviews;
import com.example.pressnews.model.News;
import com.example.pressnews.service.AnalyticsService;
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
public class InterviewController {
    @Value("${uploadDir}")
    private String uploadFolder;

    @Autowired
    private InterviewService interviewService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private XMLService xmlService;

    @GetMapping("/interviews")
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

        return "interview";
    }

    @GetMapping("/admin/add-interview")
    public String addinterview() {
        return "add-interview";
    }

    @PostMapping("admin/add-interview")
    public @ResponseBody
    ResponseEntity<?> createInterview(@RequestParam("author_id") long author_id,
                                      @RequestParam("title") String title, @RequestParam("description") String description,
                                      @RequestParam("text") String txt, @RequestParam("time_to_read") int time_to_read,
                                      @RequestParam("link_name") String link_name,
                                      @RequestParam("title_kaz") String title_kaz,
                                      @RequestParam("description_kaz") String description_kaz,
                                      @RequestParam("text_kaz") String txt_kaz,
                                      @RequestParam("link_name_kaz") String link_name_kaz,
                                      Model model, HttpServletRequest request, final @RequestParam("img") MultipartFile file) {
        try {
            String uploadDirectoryInterview = request.getServletContext().getRealPath(uploadFolder);
            String fileName = file.getOriginalFilename();
            String filePath = Paths.get(uploadDirectoryInterview, fileName).toString();
            if (fileName == null || fileName.contains("..")) {
                model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence \" + filename");
                return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
            }
            Date createDate = new Date();
            long createTime = createDate.getTime();
            try {
                File dir = new File(uploadDirectoryInterview);
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
            Interviews interview = new Interviews();
            interview.setAuthor_id(author_id);
            interview.setViews(0);
            interview.setTitle(title);
            interview.setDescription(description);
            interview.setLink_name(link_name);
            interview.setText(txt);
            interview.setImg(imageData);
            interview.setCreateDate(createDate);
            interview.setCreateTime(createTime);
            interview.setTime_to_read(time_to_read);
            interview.setDescription_kaz(description_kaz);
            interview.setLink_name_kaz(link_name_kaz);
            interview.setText_kaz(txt_kaz);
            interview.setTitle_kaz(title_kaz);
            interviewService.saveInterview(interview);
            return new ResponseEntity<>("Product Saved With File - " + fileName, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/interviews/display/{id}")
    @ResponseBody
    void showImage(@PathVariable("id") Long id, HttpServletResponse response, Optional<Interviews> interviews)
            throws ServletException, IOException {
        interviews = interviewService.getInterviewById(id);
        response.getOutputStream().write(interviews.get().getImg());
        response.getOutputStream().close();
    }

    @GetMapping("/interviews/{link_name}")
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
                    model.addAttribute("title_kaz", interviews.get().getTitle_kaz());
                    model.addAttribute("description_kaz", interviews.get().getDescription_kaz());
                    model.addAttribute("text_kaz", interviews.get().getText_kaz());
                    model.addAttribute("link_name_kaz", interviews.get().getLink_name_kaz());
                    model.addAttribute("type", 3);
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

    @GetMapping("/admin/interviews/{link_name}/edit")
    public String interviewsEdit(@PathVariable(value = "link_name") String link_name, Model model) {
        Optional<Interviews> interviews = interviewService.getInterviewsByLink_name(link_name);
        if (interviews.isPresent()) {
            model.addAttribute("id", interviews.get().getId());
            model.addAttribute("title", interviews.get().getTitle());
            model.addAttribute("description", interviews.get().getDescription());
            model.addAttribute("author_id", interviews.get().getAuthor_id());
            model.addAttribute("views", interviews.get().getViews());
            model.addAttribute("text", interviews.get().getText());
            model.addAttribute("link_name", interviews.get().getLink_name());
            model.addAttribute("title_kaz", interviews.get().getTitle_kaz());
            model.addAttribute("description_kaz", interviews.get().getDescription_kaz());
            model.addAttribute("text_kaz", interviews.get().getText_kaz());
            model.addAttribute("time_to_read", interviews.get().getTime_to_read());
            model.addAttribute("link_name_kaz", interviews.get().getLink_name_kaz());
        }
        else {
            return "redirect:/admin";
        }
        return "edit-interviews";
    }

    @PostMapping("/admin/interviews/{link_name}/edit")
    public String interviewsEditPost(@PathVariable(value = "link_name") String link_name, @RequestParam("author_id") long author_id,
                               @RequestParam("title") String title, @RequestParam("description") String description,
                               @RequestParam("text") String txt,
                               @RequestParam("title_kaz") String title_kaz,
                               @RequestParam("description_kaz") String description_kaz,
                               @RequestParam("text_kaz") String txt_kaz,
                               @RequestParam("time_to_read") Integer time_to_read,
                               @RequestParam("link_name_kaz") String link_name_kaz,
                               Model model, final @RequestParam("img") MultipartFile file) throws IOException {
        Interviews interviews = interviewService.getInterviewsByLink_name(link_name).orElseThrow();
        interviews.setTitle(title);
        interviews.setViews(interviews.getViews());
        interviews.setLink_name(link_name);
        interviews.setText(txt);
        interviews.setDescription(description);
        interviews.setDescription_kaz(description_kaz);
        interviews.setTitle_kaz(title_kaz);
        interviews.setText_kaz(txt_kaz);
        interviews.setTime_to_read(time_to_read);
        interviews.setCreateTime(interviews.getCreateTime());
        interviews.setCreateDate(interviews.getCreateDate());
        interviews.setAuthor_id(interviews.getAuthor_id());
        interviews.setLink_name_kaz(link_name_kaz);
        if (!file.isEmpty()) {
            byte[] imageData = file.getBytes();
            interviews.setImg(imageData);
        }
        else {
            interviews.setImg(interviews.getImg());
        }
        interviewService.saveInterview(interviews);
        return "redirect:/admin/interviews";
    }

    @PostMapping("/admin/interviews/{link_name}/delete")
    public String interviewsDelete(@PathVariable(value="link_name") String link_name) throws IOException {
        Interviews interviews = interviewService.getInterviewsByLink_name(link_name).orElseThrow();
        interviewService.deleteById(interviews.getId());
        return "redirect:/admin";
    }
}
