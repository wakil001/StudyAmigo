package com.studypal.FlashCard.FlashCard;

import lombok.Data;

@Data
public class Question {

    private String question;
    private String answer;
    private String category;
    private String difficulty;
}
