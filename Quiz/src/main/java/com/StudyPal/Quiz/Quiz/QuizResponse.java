package com.StudyPal.Quiz.Quiz;

import lombok.Data;

import java.util.List;

@Data
public class QuizResponse {
    private QuizDto QuizDto;
    private List<Question> questions;
}
