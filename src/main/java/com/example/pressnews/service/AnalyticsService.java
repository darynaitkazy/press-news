package com.example.pressnews.service;

import com.example.pressnews.model.Analytics;
import com.example.pressnews.model.News;

import java.util.List;
import java.util.Optional;

public interface AnalyticsService {
    void saveAnalytics(Analytics analytics);
    List<Analytics> getAllAnalytics();
    Optional<Analytics> getAnalyticsById(Long id);
}
