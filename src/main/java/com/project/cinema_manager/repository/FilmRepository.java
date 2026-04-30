package com.project.cinema_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.cinema_manager.model.Film;

public interface FilmRepository extends JpaRepository<Film, Long> {

}
