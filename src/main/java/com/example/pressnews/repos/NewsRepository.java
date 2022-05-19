package com.example.pressnews.repos;

import com.example.pressnews.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {
    @Query("SELECT n FROM News n WHERE n.link_name = ?1")
    Optional<News>getNewsByLink_name(String link_name);

    @Query(value = "SELECT * FROM News ORDER BY views DESC LIMIT 11", nativeQuery = true)
    List<News> getPopuparElevenNews();
}
