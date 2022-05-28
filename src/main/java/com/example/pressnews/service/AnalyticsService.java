package com.example.pressnews.service;

import com.example.pressnews.model.Analytics;
import com.example.pressnews.model.News;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface AnalyticsService {
    void saveAnalytics(Analytics analytics);
    Page<Analytics> getAllAnalytics(Integer page, Integer pageSize, String sortingField, String sortingDirection, String sortingFieldTime);
    Integer countAllAnalytics();
    Optional<Analytics> getAnalyticsById(Long id);
    Optional<Analytics> getAnalyticsByLink_name(String link_name);
    List<Analytics> getPopuparSevenAnalytics();
    void deleteById(Long id);
}
