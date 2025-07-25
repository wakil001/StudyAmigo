package com.studypal.FlashCard.FlashCard;

import com.studypal.FlashCard.FlashCard.Exceptions.FlashCardNotFoundException;
import com.studypal.FlashCard.FlashCard.Exceptions.NoQuestionsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("flashcard")
public class FlashCardController {
    private final FlashCardService flashCardService;

    public FlashCardController(FlashCardService flashCardService){
        this.flashCardService = flashCardService;
    }

    @GetMapping(value = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String>  generateFlashCard(@RequestParam(defaultValue = "give me random difficulty") String difficulty,
                                                               @RequestParam String description,
                                                               @RequestParam int qNum){
        return flashCardService.generateFlashCard(difficulty,qNum,description);
    }
    @GetMapping("/randomQ/{qNum}")
    public ResponseEntity<QuestionResponse> createFlashWithRandomQ(@PathVariable int qNum){
        return flashCardService.createFlashWithRandomQ(qNum);
    }

    @GetMapping("/get/randomq/QDC")
    ResponseEntity<QuestionResponse> createFlashWithRandomQDC(@RequestParam int qNum, @RequestParam String difficulty, @RequestParam String category){
        return flashCardService.createFlashWithRandomQDC(qNum,difficulty,category);
    };

    @GetMapping("/get/random/QD")
    ResponseEntity<QuestionResponse> createFlashWithQuestionsByIds(@RequestParam int qNum,@RequestParam String difficulty){
        return flashCardService.createFlashWithRandomQAndDiff(qNum,difficulty);
    };

    @PostMapping("/save")
    ResponseEntity<String> saveFlashCard(@RequestParam List<Integer> questionIds, @RequestParam String title) throws NoQuestionsException {
        return flashCardService.saveFlashCard(questionIds,title);
    }
    @PostMapping("/create/{title}")
    ResponseEntity<String> createFlashCard(@RequestBody List<Question> questions,@PathVariable String title){
        return flashCardService.createFlashCard(questions,title);
    }

    @GetMapping("/get/All/flashCards")
    public ResponseEntity<List<FlashCardGroup>> getAllFlashCards(){
       return flashCardService.getAllFlashCard();
    }

    @GetMapping("/get/groupQs/{groupId}")
    public FlashCardResponse getQuestionsByGroup(@PathVariable int groupId) throws FlashCardNotFoundException {
            return flashCardService.getQuestionsByGroup(groupId);

    }

    @PutMapping("/addQ/flashCard")
    public ResponseEntity<HttpStatus> addQFlashCard(@RequestParam int groupId,@RequestBody List<Question> questions){
        return flashCardService.addQFlashCard(groupId,questions);
    }
    @DeleteMapping("/delete/{qId}")
    public ResponseEntity<HttpStatus> deleteFlashCardQuestion(@PathVariable int qId){
        return flashCardService.deleteFlashCardQuestion(qId);
    }

    @DeleteMapping("/delete/group/{groupId}")
    public ResponseEntity<String> deleteFlashCard(@PathVariable int groupId){
        return flashCardService.deleteFlashCard(groupId);
    }
}





