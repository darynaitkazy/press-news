package com.example.pressnews.service;

import com.example.pressnews.model.Analytics;
import com.example.pressnews.model.Interviews;
import com.example.pressnews.repos.AnalyticsRepository;
import com.example.pressnews.repos.InterviewsRepository;
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
public class InterviewServiceImpl implements InterviewService {
    @Autowired
    private InterviewsRepository interviewsRepository;

    @Override
    public void saveInterview(Interviews interviews) {
        interviewsRepository.save(interviews);
    }

    @Override
    public Page<Interviews> getAllInterviews(Integer page, Integer pageSize, String sortingField, String sortingDirection,
                                           String sortingFieldTime) {
        Sort sort = Sort.by(Sort.Direction.valueOf(sortingDirection), sortingField, sortingFieldTime);
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        return interviewsRepository.findAll(pageable);
    }

    @Override
    public Integer countAllInterviews() {
        return interviewsRepository.countAllInterviews();
    }

    @Override
    public Optional<Interviews> getInterviewById(Long id) {
        return interviewsRepository.findById(id);
    }

    @Override
    public Optional<Interviews> getInterviewsByLink_name(String link_name) {
        return interviewsRepository.getInterviewsByLink_name(link_name);
    }

    @Override
    public void deleteById(Long id) {
        interviewsRepository.deleteById(id);
    }
}
