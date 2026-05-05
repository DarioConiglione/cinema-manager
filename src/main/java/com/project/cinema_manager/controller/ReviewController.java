package com.project.cinema_manager.controller;

import com.project.cinema_manager.model.Film;
import com.project.cinema_manager.model.Review;
import com.project.cinema_manager.repository.FilmRepository;
import com.project.cinema_manager.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/films")
public class ReviewController {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    // CREATE REVIEW (Form): Mostra il form per aggiungere una recensione
    @GetMapping("/show/{filmId}/reviews/create")
    public String createReview(@PathVariable("filmId") Integer filmId, Model model) {
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new IllegalArgumentException("Film non trovato"));
        model.addAttribute("film", film);
        model.addAttribute("review", new Review());
        return "review/create";
    }

    // CREATE REVIEW (Action): Salva la recensione nel database
    @PostMapping("/show/{filmId}/reviews/store")
    public String storeReview(@PathVariable("filmId") Integer filmId, @ModelAttribute("review") Review review) {
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new IllegalArgumentException("Film non trovato"));
        review.setFilm(film);
        reviewRepository.save(review);
        return "redirect:/admin/films/show/" + filmId;
    }

    // EDIT: Mostra il form per modificare la recensione
    @GetMapping("/reviews/edit/{id}")
    public String editReview(@PathVariable Integer id, Model model) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recensione non trovata"));
        model.addAttribute("review", review);
        model.addAttribute("film", review.getFilm());
        return "review/edit";
    }

    // UPDATE: Salva i dati modificati della recensione
    @PostMapping("/reviews/update/{id}")
    public String updateReview(@PathVariable Integer id, @ModelAttribute("review") Review reviewDetails) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recensione non trovata"));
        review.setAuthor(reviewDetails.getAuthor());
        review.setRating(reviewDetails.getRating());
        review.setContent(reviewDetails.getContent());
        reviewRepository.save(review);
        return "redirect:/admin/films/show/" + review.getFilm().getId();
    }

    // DELETE: Elimina la recensione (corretto il path per evitare duplicazioni)
    @GetMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable Integer id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recensione non trovata"));
        Integer filmId = review.getFilm().getId();
        reviewRepository.deleteById(id);
        return "redirect:/admin/films/show/" + filmId;
    }
}