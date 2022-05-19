package com.example.pressnews.service;

import com.example.pressnews.model.Analytics;
import com.example.pressnews.model.Interviews;
import com.example.pressnews.model.News;
import com.example.pressnews.repos.AnalyticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AnalyticsServiceImpl implements AnalyticsService{
    @Autowired
    private AnalyticsRepository analyticsRepository;

    @Override
    public void saveAnalytics(Analytics analytics) {
        analyticsRepository.save(analytics);
    }

    @Override
    public List<Analytics> getAllAnalytics() {
        return analyticsRepository.findAll();
    }

    @Override
    public Optional<Analytics> getAnalyticsById(Long id) {
        return analyticsRepository.findById(id);
    }



}
