package com.studypal.FlashCard.FlashCard;


import com.studypal.FlashCard.FlashCard.Exceptions.FlashCardNotFoundException;
import com.studypal.FlashCard.FlashCard.Exceptions.NoQuestionsException;
import com.studypal.FlashCard.FlashCard.Exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericExceptions(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoQuestionsException.class)
    public ResponseEntity<String> handleNoQuestionException(NoQuestionsException ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FlashCardNotFoundException.class)
    public ResponseEntity<String> handleFlashCardNotFoundException(FlashCardNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
    }

}
