package com.example.pressnews.service;

import com.example.pressnews.model.News;
import com.example.pressnews.model.Opinions;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface OpinionsService {
    void saveOpinions(Opinions opinions);
    Page<Opinions> getAllOpinions(Integer page, Integer pageSize, String sortingField, String sortingDirection, String sortingFieldTime);
    Optional<Opinions> getOpinionsById(Long id);
    void deleteById(Long id);
    Optional<Opinions> getOpinionsByLink_name(String link_name);
    Integer countAllOpinions();
}
