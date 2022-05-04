package com.example.pressnews.repos;

import com.example.pressnews.model.Interviews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewsRepository extends JpaRepository<Interviews, Long> {
}
