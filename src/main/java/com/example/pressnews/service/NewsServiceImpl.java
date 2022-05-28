package com.example.pressnews.service;

import com.example.pressnews.model.News;
import com.example.pressnews.repos.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsRepository newsRepository;



    @Override
    public void saveNews(News news) {
        newsRepository.save(news);
    }

    @Override
    public Page<News> getAllNews(Integer page, Integer pageSize, String sortingField, String sortingDirection,
                                 String sortingFieldTime) {
        Sort sort = Sort.by(Sort.Direction.valueOf(sortingDirection), sortingField, sortingFieldTime);
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        return newsRepository.findAll(pageable);
    }

    @Override
    public List<News> findByKeyword(String keyword) {
        return newsRepository.findByKeyword(keyword);
    }

    @Override
    public Optional<News> getNewsById(Long id) {
        return newsRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        newsRepository.deleteById(id);
    }

    @Override
    public Optional<News> getNewsByLink_name(String link_name) {
        return newsRepository.getNewsByLink_name(link_name);
    }

    @Override
    public List<News> getPopularElevenNews() {
        return newsRepository.getPopularElevenNews();
    }

    @Override
    public List<News> getPopularFourteenNews() {
        return newsRepository.getPopularFourteenNews();
    }

    @Override
    public List<News> getRecentFourteenNews() {
        return newsRepository.getRecentFourteenNews();
    }

    @Override
    public Integer countAllNews() {
        return newsRepository.countAllNews();
    }


}
