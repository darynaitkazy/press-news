package com.example.pressnews.repos;

import com.example.pressnews.model.News;
import com.example.pressnews.model.Opinions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OpinionsRepository extends JpaRepository<Opinions, Long> {
    @Query("SELECT n FROM Opinions n WHERE n.link_name = ?1")
    Optional<Opinions> getOpinionsByLink_name(String link_name);

    @Query(value = "SELECT COUNT(*) FROM Opinions", nativeQuery = true)
    Integer countAllOpinions();
}
