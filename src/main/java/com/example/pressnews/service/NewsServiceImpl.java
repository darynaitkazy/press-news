package com.example.pressnews.service;

import com.example.pressnews.model.News;
import com.example.pressnews.repos.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public Page<News> getAllNews(Integer pageSize) {
        return newsRepository.findAll(Pageable.ofSize(pageSize));
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
    public List<News> getPopuparElevenNews() {
        return newsRepository.getPopuparElevenNews();
    }
}
