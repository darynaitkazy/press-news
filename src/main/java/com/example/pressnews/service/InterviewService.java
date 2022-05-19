package com.example.pressnews.service;

import com.example.pressnews.model.Analytics;
import com.example.pressnews.model.Interviews;

import java.util.List;
import java.util.Optional;

public interface InterviewService {
    void saveInterview(Interviews interviews);
    List<Interviews> getAllInterviews();
    Optional<Interviews> getInterviewById(Long id);
}
