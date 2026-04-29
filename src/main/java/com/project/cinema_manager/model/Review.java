package com.project.cinema_manager.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String author;

    @Column(length = 500)
    private String content;

    private int rating; // Es: da 1 a 5

    // Relazione N-1: Molte recensioni appartengono a un solo film
    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;
}
