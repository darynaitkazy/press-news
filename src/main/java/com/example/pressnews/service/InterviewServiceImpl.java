package com.example.pressnews.service;

import com.example.pressnews.model.Analytics;
import com.example.pressnews.model.Interviews;
import com.example.pressnews.repos.AnalyticsRepository;
import com.example.pressnews.repos.InterviewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InterviewServiceImpl implements InterviewService {
    @Autowired
    private InterviewsRepository interviewsRepository;

    @Override
    public void saveInterview(Interviews interviews) {
        interviewsRepository.save(interviews);
    }

    @Override
    public List<Interviews> getAllInterviews() {
        return interviewsRepository.findAll();
    }

    @Override
    public Optional<Interviews> getInterviewById(Long id) {
        return interviewsRepository.findById(id);
    }
}
