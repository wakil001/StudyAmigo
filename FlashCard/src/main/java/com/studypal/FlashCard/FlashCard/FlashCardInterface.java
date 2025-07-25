package com.studypal.FlashCard.FlashCard;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "QUESTIONS")
public interface FlashCardInterface {

    @GetMapping("questions/get/randomq/QDC")
    ResponseEntity<List<Integer>> getFlashWithRandomQDC(@RequestParam int qNum, @RequestParam String difficulty,@RequestParam String category);

    @GetMapping("questions/get/questions/byid")
    ResponseEntity<List<Question>> getQuestionsByIds(@RequestParam List<Integer> questionIds);

    @GetMapping("questions/get/randomq/Q/{qNum}")
    ResponseEntity<List<Integer>> getRandomQuestions(@RequestParam int qNum);

    @GetMapping("questions/get/randomq/QD")
    ResponseEntity<List<Integer>>getRandomQuestionsByDiff(@RequestParam int qNum,@RequestParam String difficulty);

    @GetMapping("questions/save/questions")
    ResponseEntity<List<Integer>> saveQuestions(@RequestBody List<Question> questions);

    @DeleteMapping("questions/delete/question/{ids}")
    ResponseEntity<HttpStatus> deleteQuestionById(@PathVariable int ids);
}
