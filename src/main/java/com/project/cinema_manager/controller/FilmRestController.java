package com.project.cinema_manager.controller;

import com.project.cinema_manager.model.Film;
import com.project.cinema_manager.model.Review;
import com.project.cinema_manager.repository.FilmRepository;
import com.project.cinema_manager.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/films")
public class FilmRestController {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    // Index: Lista di tutti i film
    @GetMapping
    public List<Film> index() {
        return filmRepository.findAll();
    }

    // Show: Dettagli di un singolo film
    @GetMapping("/{id}")
    public ResponseEntity<Film> show(@PathVariable Integer id) {
        return filmRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: Creazione
    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmRepository.save(film);
    }

    // PUT: Aggiornamento
    @PutMapping("/{id}")
    public ResponseEntity<Film> update(@PathVariable Integer id, @RequestBody Film filmDetails) {
        return filmRepository.findById(id).map(film -> {
            film.setTitle(filmDetails.getTitle());
            film.setDescription(filmDetails.getDescription());
            return ResponseEntity.ok(filmRepository.save(film));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE: Eliminazione
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (filmRepository.existsById(id)) {
            filmRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // POST: Aggiungi una recensione a un film specifico
    @PostMapping("/{id}/reviews")
    public ResponseEntity<Review> addReview(@PathVariable Integer id, @RequestBody Review review) {
        return filmRepository.findById(id).map(film -> {
            review.setFilm(film); // Collega la recensione al film trovato
            Review savedReview = reviewRepository.save(review);
            return ResponseEntity.ok(savedReview);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
