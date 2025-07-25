package com.studypal.Questions.Question;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("questions")
public class QuestionController {
    private final QuestionService questionService;
    public QuestionController(QuestionService questionService){
        this.questionService = questionService;
    }
    @GetMapping("/getAllQuestions")
    public ResponseEntity<List<Question>> getAllquestions(){
    return questionService.getAllquestions();

    }
    @GetMapping("get/randomq/QDC")
    ResponseEntity<List<Integer>> getRandomQDC(@RequestParam int qNum, @RequestParam String difficulty,@RequestParam String category){
        return questionService.getRandomQDC(qNum,difficulty,category);

    };
    @GetMapping("get/questions/byid")
    ResponseEntity<List<Question>> getQuestionsByIds(@RequestParam List<Integer> questionIds) {
        return questionService.getQuestionsByIds(questionIds);
    };
    @GetMapping("get/randomq/Q/{qNum}")
    ResponseEntity<List<Integer>> getRandomQuestions(@PathVariable int qNum){
        return questionService.getRandomQ(qNum);
    };
    @GetMapping("get/randomq/QD")
    ResponseEntity<List<Integer>> getRandomQuestionsByDiff(@RequestParam int qNum,@RequestParam String difficulty){
        return questionService.getRandomQD(qNum,difficulty);
    };
    @PostMapping("save/questions")
    ResponseEntity<List<Integer>> saveQuestions(@RequestBody List<Question> questions){
        return questionService.saveQuestions(questions);
    };
    @DeleteMapping("deleteQuestions")
    ResponseEntity<HttpStatus> deleteQuestions(@RequestBody List<Integer> ids){
        return questionService.deleteQuestions(ids);
    }
    @PutMapping("update/question")
    ResponseEntity<HttpStatus> updateQuestion(@RequestBody Question question,@RequestParam int questionId){
        return questionService.updateQuestion(questionId,question);
    }

    @DeleteMapping("delete/question/{qId}")
    public ResponseEntity<HttpStatus> deleteQuestionById(@PathVariable int qId){
        questionService.deleteQuestionById(qId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("delete/all")
    public ResponseEntity<HttpStatus> deleteAllQuestions(){
       return questionService.deleteAllQuestions();

    }
}
