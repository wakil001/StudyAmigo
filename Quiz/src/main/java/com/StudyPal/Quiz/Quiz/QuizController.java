package com.StudyPal.Quiz.Quiz;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz")
public class QuizController{
    private final QuizService quizService;

    public QuizController(QuizService quizService){
        this.quizService = quizService;
    }

    @GetMapping("get/random/Q/{qNum}")
   ResponseEntity<QuestionResponse> createQuizWithRandomQ(@PathVariable int qNum){
        return quizService.createQuizWithRandomQ(qNum);

   }
   @GetMapping(value= "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> generateQuiz(@RequestParam(defaultValue = "give me random difficulty") String difficulty , @RequestParam int qNum, @RequestParam String description){
        return quizService.generateQuiz(difficulty,qNum,description);
    }
    @GetMapping("get/randomq/QD")
    ResponseEntity<QuestionResponse> createQuizWithRandomQAndDiff(@RequestParam int qNum, @RequestParam String difficulty){
        return quizService.createQuizWithRandomQAndDiff(qNum,difficulty);
    };

    @GetMapping("get/randomq/QDC")
    ResponseEntity<QuestionResponse> getRandomQDC(@RequestParam int qNum, @RequestParam String difficulty, @RequestParam String category){
        return quizService.createQuizWithRandomQDC(qNum,difficulty,category);
    };

    @PostMapping("save/{title}")
    public ResponseEntity<String> saveQuiz(@RequestBody List<Integer> questionIds,@PathVariable String title){
        return quizService.saveQuiz(questionIds,title);
    }

    @GetMapping("getQuestionGroup/{groupId}")
    public QuizResponse getQuestionsByGroup(@PathVariable int groupId){
        return quizService.getQuestionsByGroup(groupId);
    }

    @PutMapping("update/question")
    public ResponseEntity<HttpStatus> updateQuestion(@RequestBody Question question,@RequestParam int questionId ){
        return quizService.updateQuestion(question,questionId);
    }

    @PutMapping("addQ/quiz/{groupId}")
    public ResponseEntity<HttpStatus> addQtoQuiz(@RequestBody List<Question> questions, @PathVariable int groupId){
        return quizService.addQtoQuiz(groupId,questions);
    }

    @DeleteMapping("delete/question/{questionId}")
    public ResponseEntity<HttpStatus>  deleteQuestionById(@PathVariable int questionId){
            return quizService.deleteQuizQuestion(questionId);
    }
    @DeleteMapping("deleteQuiz/{groupId}")
    public ResponseEntity<String> deleteQuiz(@PathVariable int groupId){
        return quizService.deleteQuiz(groupId);
    }

    @GetMapping("get/allQuiz")
    public ResponseEntity<List<QuizGroup>> getAllQuiz(){
        return quizService.getAllQuiz();
    }
    @PostMapping("create/Quiz/{title}")
    public ResponseEntity<List<Question>> createQuiz(@RequestBody List<Question> questions,@PathVariable String title){
        return quizService.createQuiz(questions,title);
    }

}
