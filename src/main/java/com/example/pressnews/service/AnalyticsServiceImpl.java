package com.example.pressnews.service;

import com.example.pressnews.model.Analytics;
import com.example.pressnews.model.Interviews;
import com.example.pressnews.model.News;
import com.example.pressnews.repos.AnalyticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<Analytics> getAllAnalytics(Integer page, Integer pageSize, String sortingField, String sortingDirection,
                                 String sortingFieldTime) {
        Sort sort = Sort.by(Sort.Direction.valueOf(sortingDirection), sortingField, sortingFieldTime);
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        return analyticsRepository.findAll(pageable);
    }

    @Override
    public Integer countAllAnalytics() {
        return analyticsRepository.countAllAnalytics();
    }

    @Override
    public Optional<Analytics> getAnalyticsById(Long id) {
        return analyticsRepository.findById(id);
    }

    @Override
    public Optional<Analytics> getAnalyticsByLink_name(String link_name) {
        return analyticsRepository.getAnalyticsByLink_name(link_name);
    }

    @Override
    public List<Analytics> getPopuparSevenAnalytics() {
        return analyticsRepository.getPopuparSevenAnalytics();
    }

    @Override
    public void deleteById(Long id) {
        analyticsRepository.deleteById(id);
    }

}
