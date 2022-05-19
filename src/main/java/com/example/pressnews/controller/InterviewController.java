package com.example.pressnews.controller;

import com.example.pressnews.model.Analytics;
import com.example.pressnews.model.Interviews;
import com.example.pressnews.service.AnalyticsService;
import com.example.pressnews.service.InterviewService;
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
public class InterviewController {
    @Value("${uploadDir}")
    private String uploadFolder;

    @Autowired
    private InterviewService interviewService;

    @GetMapping("/interviews")
    public String interviews(Model model) {
        List<Interviews> interviews = interviewService.getAllInterviews();
        model.addAttribute("interviews", interviews);
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
                                 Model model, HttpServletRequest request, final @RequestParam("img") MultipartFile file) {
        try {
            String uploadDirectoryInterview = request.getServletContext().getRealPath(uploadFolder);
            String fileName = file.getOriginalFilename();
            String filePath = Paths.get(uploadDirectoryInterview, fileName).toString();
            if (fileName == null || fileName.contains("..")) {
                model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence \" + filename");
                return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
            }
            String[] titles = title.split(",");
            String[] descriptions = description.split(",");
            String[] txts = txt.split(",");
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
            interview.setTitle(titles[0]);
            interview.setDescription(descriptions[0]);
            interview.setText(txts[0]);
            interview.setImg(imageData);
            interview.setCreateDate(createDate);
            interview.setCreateTime(createTime);
            interview.setTime_to_read(time_to_read);
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

    @GetMapping("/interviews/{id}")
    String showNewsId(@PathVariable("id") Long id, Optional<Interviews> interviews, Model model) {
        try {
            if (id != 0) {
                interviews = interviewService.getInterviewById(id);
                if (interviews.isPresent()) {
                    model.addAttribute("id", interviews.get().getId());
                    model.addAttribute("author_id", interviews.get().getAuthor_id());
                    model.addAttribute("title", interviews.get().getTitle());
                    model.addAttribute("createDate", interviews.get().getCreateDate());
                    model.addAttribute("createTime", interviews.get().getCreateTime());
                    model.addAttribute("description", interviews.get().getDescription());
                    model.addAttribute("text", interviews.get().getText());
                    model.addAttribute("time_to_read", interviews.get().getTime_to_read());
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
