package com.StudyPal.Quiz.Quiz;

import lombok.Data;

import java.util.List;

@Data
public class QuestionResponse {
    private List<Integer> questionsIds;
    private List<Question> questions;

}
