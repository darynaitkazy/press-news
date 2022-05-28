package com.example.pressnews.service;

import com.example.pressnews.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

public interface NewsService {
    void saveNews(News news);
    Page<News> getAllNews(Integer page, Integer pageSize, String sortingField, String sortingDirection, String sortingFieldTime);
    Optional<News> getNewsById(Long id);
    void deleteById(Long id);
    Optional<News> getNewsByLink_name(String link_name);
    Integer countAllNews();
    List<News> getPopularElevenNews();
    List<News> getPopularFourteenNews();
    List<News> getRecentFourteenNews();
    List<News> findByKeyword(String keyword);
}
