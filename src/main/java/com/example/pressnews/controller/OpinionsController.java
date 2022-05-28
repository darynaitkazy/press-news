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
public class OpinionsController {
    @Value("${uploadDir}")
    private String uploadFolder;

    @Autowired
    private OpinionsService opinionsService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private XMLService xmlService;

    @GetMapping("/opinions")
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

        return "opinions";
    }

    @GetMapping("/admin/add-opinions")
    public String addopinions() {
        return "add-opinions";
    }

    @PostMapping("admin/add-opinions")
    public @ResponseBody
    ResponseEntity<?> createOpinions(@RequestParam("author_id") long author_id,
                                 @RequestParam("author") String author, @RequestParam("title") String title,
                                 @RequestParam("text") String txt,
                                 @RequestParam("link_name") String link_name,
                                 @RequestParam("author_position") String author_position,
                                 @RequestParam("author_position_kaz") String author_position_kaz,
                                     @RequestParam("title_kaz") String title_kaz,
                                     @RequestParam("text_kaz") String text_kaz,
                                 @RequestParam("link_name_kaz") String link_name_kaz,
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
            Opinions opinions = new Opinions();
            opinions.setAuthor_id(author_id);
            opinions.setTitle(title);
            opinions.setText(txt);
            opinions.setAuthor(author);
            opinions.setLink_name(link_name);
            opinions.setImg(imageData);
            opinions.setCreateDate(createDate);
            opinions.setCreateTime(createTime);
            opinions.setAuthor_position(author_position);
            opinions.setViews(0);
            opinions.setAuthor_position_kaz(author_position_kaz);
            opinions.setText_kaz(text_kaz);
            opinions.setTitle_kaz(title_kaz);
            opinions.setLink_name_kaz(link_name_kaz);
            opinionsService.saveOpinions(opinions);
            return new ResponseEntity<>("Product Saved With File - " + fileName, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/opinions/display/{id}")
    @ResponseBody
    void showImage(@PathVariable("id") Long id, HttpServletResponse response, Optional<Opinions> opinions)
            throws ServletException, IOException {
        opinions = opinionsService.getOpinionsById(id);
        response.getOutputStream().write(opinions.get().getImg());
        response.getOutputStream().close();
    }

    @GetMapping("opinions/{link_name}")
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

    @GetMapping("/admin/opinions/{link_name}/edit")
    public String newsEdit(@PathVariable(value = "link_name") String link_name, Model model) {
        Optional<Opinions> opinions = opinionsService.getOpinionsByLink_name(link_name);
        if (opinions.isPresent()) {
            model.addAttribute("id", opinions.get().getId());
            model.addAttribute("title", opinions.get().getTitle());
            model.addAttribute("author", opinions.get().getAuthor());
            model.addAttribute("author_position", opinions.get().getAuthor_position());
            model.addAttribute("author_id", opinions.get().getAuthor_id());
            model.addAttribute("text", opinions.get().getText());
            model.addAttribute("link_name", opinions.get().getLink_name());
            model.addAttribute("views", opinions.get().getViews());
            model.addAttribute("author_position_kaz", opinions.get().getAuthor_position_kaz());
            model.addAttribute("title_kaz", opinions.get().getTitle_kaz());
            model.addAttribute("text_kaz", opinions.get().getText_kaz());
            model.addAttribute("link_name_kaz", opinions.get().getLink_name_kaz());
        }
        else {
            return "redirect:/admin";
        }
        return "edit-opinions";
    }

    @PostMapping("/admin/opinions/{link_name}/edit")
    public String opinionsEditPost(@PathVariable(value = "link_name") String link_name, @RequestParam("author_id") long author_id,
                               @RequestParam("title") String title, @RequestParam("author") String author,
                               @RequestParam("text") String txt,
                               @RequestParam("author_position") String author_position,
                               @RequestParam("author_position_kaz") String author_position_kaz,
                               @RequestParam("title_kaz") String title_kaz,
                               @RequestParam("text_kaz") String txt_kaz,
                               @RequestParam("link_name_kaz") String link_name_kaz,
                               Model model, final @RequestParam("img") MultipartFile file) throws IOException {
        Opinions opinions = opinionsService.getOpinionsByLink_name(link_name).orElseThrow();
        opinions.setTitle(title);
        opinions.setLink_name(link_name);
        opinions.setText(txt);
        opinions.setAuthor(author);
        opinions.setCreateTime(opinions.getCreateTime());
        opinions.setCreateDate(opinions.getCreateDate());
        opinions.setAuthor_id(opinions.getAuthor_id());
        opinions.setAuthor_position(author_position);
        opinions.setAuthor_position_kaz(author_position_kaz);
        opinions.setTitle_kaz(title_kaz);
        opinions.setText_kaz(txt_kaz);
        opinions.setLink_name_kaz(link_name_kaz);

        if (!file.isEmpty()) {
            byte[] imageData = file.getBytes();
            opinions.setImg(imageData);
        }
        else {
            opinions.setImg(opinions.getImg());
        }
        opinionsService.saveOpinions(opinions);
        return "redirect:/admin/opinions";
    }

    @PostMapping("/admin/opinions/{link_name}/delete")
    public String opinionsDelete(@PathVariable(value="link_name") String link_name) throws IOException {
        Opinions opinions = opinionsService.getOpinionsByLink_name(link_name).orElseThrow();
        opinionsService.deleteById(opinions.getId());
        return "redirect:/admin";
    }

}
