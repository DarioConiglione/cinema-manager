package com.project.cinema_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.cinema_manager.model.Review;

public interface ReviewRepository extends JpaRepository {

    List<Review> findByFilmId(Long filmId);
}