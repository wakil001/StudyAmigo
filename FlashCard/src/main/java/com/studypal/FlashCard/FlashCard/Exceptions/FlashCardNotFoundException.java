package com.studypal.FlashCard.FlashCard.Exceptions;

public class FlashCardNotFoundException extends Exception{
    public FlashCardNotFoundException(String message){
        super("flashCard not found");
    }
}
