package com.project.cinema_manager.controller;

import com.project.cinema_manager.model.Film;
import com.project.cinema_manager.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/films") // Tutte le rotte qui sotto iniziano con /admin/films
public class FilmController {

    @Autowired
    private FilmRepository filmRepository;

    // 1. READ: Visualizza la lista di tutti i film
    @GetMapping
    public String index(Model model) {
        List<Film> films = filmRepository.findAll();
        model.addAttribute("films", films);
        return "admin/index"; // Cercherà il file src/main/resources/templates/admin/index.html
    }

    // 1. SHOW: Visualizza i dettagli di un singolo film
    @GetMapping("/show/{id}")
    public String show(@PathVariable Integer id, Model model) {
        Film film = filmRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Film non trovato"));
        model.addAttribute("film", film);
        return "admin/show";
    }

    // 2. CREATE (Form): Mostra il form per aggiungere un nuovo film
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("film", new Film());
        return "admin/create";
    }

    // 3. CREATE (Action): Salva il film nel database
    @PostMapping("/store")
    public String store(@ModelAttribute("film") Film film) {
        filmRepository.save(film);
        return "redirect:/admin/films";
    }

    // 4. DELETE: Rimuove un film tramite ID
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        filmRepository.deleteById(id);
        return "redirect:/admin/films";
    }
}