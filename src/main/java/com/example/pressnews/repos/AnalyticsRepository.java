package com.example.pressnews.repos;

import com.example.pressnews.model.Analytics;
import com.example.pressnews.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {
    @Query("SELECT n FROM Analytics n WHERE n.link_name = ?1")
    Optional<Analytics> getAnalyticsByLink_name(String link_name);

    @Query(value = "SELECT * FROM Analytics ORDER BY views DESC LIMIT 7", nativeQuery = true)
    List<Analytics> getPopuparSevenAnalytics();
}
