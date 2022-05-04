package com.example.pressnews.repos;

import com.example.pressnews.model.Opinions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpinionsRepository extends JpaRepository<Opinions, Long> {
}
