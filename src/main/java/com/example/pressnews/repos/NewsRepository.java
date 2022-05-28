package com.example.pressnews.repos;

import com.example.pressnews.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {
    @Query("SELECT n FROM News n WHERE n.link_name = ?1")
    Optional<News>getNewsByLink_name(String link_name);

    @Query(value = "SELECT * FROM News ORDER BY views DESC LIMIT 11", nativeQuery = true)
    List<News> getPopularElevenNews();

    @Query(value = "SELECT * FROM News ORDER BY views DESC LIMIT 14", nativeQuery = true)
    List<News> getPopularFourteenNews();

    @Query(value = "SELECT * FROM News ORDER BY create_date DESC LIMIT 14", nativeQuery = true)
    List<News> getRecentFourteenNews();

    @Query(value = "SELECT COUNT(*) FROM News", nativeQuery = true)
    Integer countAllNews();

    @Query(value = "select COUNT(*) from News n where n.title like %:keyword or n.description like %:keyword or n.text like %:keyword", nativeQuery = true)
    Integer countAllNewsByKeyword();

    @Query(value = "select * from News n where n.title like %:keyword% or n.description like %:keyword% or n.text like %:keyword%", nativeQuery = true)
    List<News>findByKeyword(@Param("keyword") String keyword);

}
