package com.example.pressnews.service;

import com.example.pressnews.model.News;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface NewsService {
    void saveNews(News news);
    Page<News> getAllNews(Integer pageSize);
    Optional<News> getNewsById(Long id);
    void deleteById(Long id);
    Optional<News> getNewsByLink_name(String link_name);
    List<News> getPopuparElevenNews();
}
