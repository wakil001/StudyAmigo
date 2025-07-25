package com.studypal.FlashCard.FlashCard;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "FlashCard")
public class FlashCardGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", unique = true)
    private String title;


    @ElementCollection
    @CollectionTable(name = "flashcard_questions", joinColumns = @JoinColumn(name = "flashcard_id"))
    @Column(name = "question_id", unique = true)
    private List<Integer> questionIds;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
