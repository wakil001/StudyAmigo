package com.studypal.FlashCard.FlashCard;

import lombok.Data;

import java.util.List;
@Data
public class QuestionResponse {
    private List<Integer> questionsIds;
    private List<Question> questions;

    public QuestionResponse(List<Question> questions) {
    }

    public QuestionResponse(String s) {
    }
}
