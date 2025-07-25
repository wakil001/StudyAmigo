package com.StudyPal.Quiz.Quiz;

import lombok.Data;

@Data
public class Question {
    private int id;
    private String question;
    private String answer;
    private String option1;
    private String option2;
    private String option3;
    private String difficulty;
    private String category;
}
