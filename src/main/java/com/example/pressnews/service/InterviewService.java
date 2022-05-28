package com.example.pressnews.service;

import com.example.pressnews.model.Analytics;
import com.example.pressnews.model.Interviews;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface InterviewService {
    void saveInterview(Interviews interviews);
    Page<Interviews> getAllInterviews(Integer page, Integer pageSize, String sortingField, String sortingDirection, String sortingFieldTime);
    Integer countAllInterviews();
    Optional<Interviews> getInterviewById(Long id);
    Optional<Interviews> getInterviewsByLink_name(String link_name);
    void deleteById(Long id);
}
