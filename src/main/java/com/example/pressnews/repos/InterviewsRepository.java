package com.example.pressnews.repos;

import com.example.pressnews.model.Analytics;
import com.example.pressnews.model.Interviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InterviewsRepository extends JpaRepository<Interviews, Long> {
    @Query("SELECT n FROM Interviews n WHERE n.link_name = ?1")
    Optional<Interviews> getInterviewsByLink_name(String link_name);

    @Query(value = "SELECT COUNT(*) FROM Interviews", nativeQuery = true)
    Integer countAllInterviews();
}
