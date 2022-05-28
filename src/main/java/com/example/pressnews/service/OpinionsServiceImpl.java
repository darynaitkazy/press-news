package com.example.pressnews.service;

import com.example.pressnews.model.Opinions;
import com.example.pressnews.repos.OpinionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class OpinionsServiceImpl implements OpinionsService{

    @Autowired
    private OpinionsRepository opinionsRepository;

    @Override
    public void saveOpinions(Opinions opinions) {
        opinionsRepository.save(opinions);
    }

    @Override
    public Page<Opinions> getAllOpinions(Integer page, Integer pageSize, String sortingField, String sortingDirection, String sortingFieldTime) {
        Sort sort = Sort.by(Sort.Direction.valueOf(sortingDirection), sortingField, sortingFieldTime);
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        return opinionsRepository.findAll(pageable);
    }

    @Override
    public Optional<Opinions> getOpinionsById(Long id) {
        return opinionsRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        opinionsRepository.deleteById(id);
    }

    @Override
    public Optional<Opinions> getOpinionsByLink_name(String link_name) {
        return opinionsRepository.getOpinionsByLink_name(link_name);
    }

    @Override
    public Integer countAllOpinions() {
        return opinionsRepository.countAllOpinions();
    }
}
