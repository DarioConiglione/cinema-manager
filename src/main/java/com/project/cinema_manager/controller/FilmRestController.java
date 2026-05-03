package com.project.cinema_manager.controller;

import com.project.cinema_manager.model.Film;
import com.project.cinema_manager.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/films")
@CrossOrigin(origins = "http://localhost:5173") // Permette a React (Vite) di chiamare Spring
public class FilmRestController {

    @Autowired
    private FilmRepository filmRepository;

    // Lista film per la home di React
    @GetMapping
    public List<Film> getAll() {
        return filmRepository.findAll();
    }

    // Dettaglio singolo film + sue recensioni
    @GetMapping("/{id}")
    public ResponseEntity<Film> getById(@PathVariable Integer id) {
        return filmRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

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
}
