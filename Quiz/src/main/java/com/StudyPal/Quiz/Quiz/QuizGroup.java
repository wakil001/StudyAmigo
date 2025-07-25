package com.StudyPal.Quiz.Quiz;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class QuizGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;

    @ElementCollection
    @CollectionTable(name="Quiz_group_Questions")
    private List<Integer> questionsId;
}
