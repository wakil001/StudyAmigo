package com.studypal.FlashCard.FlashCard;


import com.studypal.FlashCard.FlashCard.Exceptions.FlashCardNotFoundException;
import com.studypal.FlashCard.FlashCard.Exceptions.NoQuestionsException;
import org.modelmapper.ModelMapper;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
public class FlashCardService {

    private final FlashCardDao flashCardDao;
    private final FlashCardInterface flashCardInterface;
    private final ChatModel chatModel;

    public FlashCardService(ModelMapper modelMapper, FlashCardInterface flashCardInterface,
                            FlashCardDao flashCardDao,
                            ChatModel chatModel){
        this.flashCardInterface = flashCardInterface;
        this.flashCardDao = flashCardDao;
        this.chatModel =chatModel;
    }



    public ResponseEntity<String> generateFlashCard(String difficulty, int qNum, String description) {

        if (qNum <= 0) {
            return ResponseEntity.badRequest()
                    .body("{\"error\": \"qNum must be a positive integer\"}");
        }
        if (description == null || description.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("{\"error\": \"Description cannot be empty\"}");
        }

        String template = """
            Instructions:
            - Valid difficulties are: puzzling, medium, easy,
            - Generate {qNum} flashcard questions based on the topic: {description}
            - Set the difficulty to: {difficulty}
            - Set the category to: AiGen
            - Remove any * or # from the response
            - Return the response in the following JSON format:
            ```json
            
               
                        "question": "Generated question based on {description}",
                        "answer": "Generated answer based on {description}",
                        "category": "AiGen",
                        "difficulty": "{difficulty}"
                 
         
            ```
            Ensure the response contains exactly {qNum} flashcard objects in the "flashcards" array.
            """;

        PromptTemplate temp = new PromptTemplate(template);

        Map<String, Object> params = Map.of(
                "qNum", qNum,
                "difficulty", difficulty,
                "description", description
        );

        Prompt prompt = temp.create(params);
        String result = chatModel.call(prompt).getResult().getOutput().toString();


        return ResponseEntity.ok().body(result);

    }
    public ResponseEntity<QuestionResponse> createFlashWithRandomQDC(int qNum,String difficulty,String category){
        try {
            List<Integer> questionsIds = flashCardInterface.getFlashWithRandomQDC(qNum,difficulty,category).getBody();
            List<Question> questions = flashCardInterface.getQuestionsByIds(questionsIds).getBody();
            return createQuestionResponse(questionsIds,questions);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public ResponseEntity<QuestionResponse> createFlashWithRandomQ(int qNum){
        try {
            List<Integer> questionIds = flashCardInterface.getRandomQuestions(qNum).getBody();
            List<Question> questions = flashCardInterface.getQuestionsByIds(questionIds).getBody();
            return createQuestionResponse(questionIds,questions);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<QuestionResponse> createFlashWithRandomQAndDiff(int qNum,String difficulty){

        try {
            List<Integer> questionIds = flashCardInterface.getRandomQuestionsByDiff(qNum,difficulty).getBody();
            List<Question> questions = flashCardInterface.getQuestionsByIds(questionIds).getBody();
            return createQuestionResponse(questionIds,questions);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<String> saveFlashCard(List<Integer> questionIds, String title) throws NoQuestionsException {
        if (questionIds != null) {
            FlashCardGroup flashCardGroup = new FlashCardGroup();
            flashCardGroup.setTitle(title);
            flashCardGroup.setQuestionIds(questionIds);
            flashCardDao.save(flashCardGroup);
            return ResponseEntity.status(HttpStatus.OK).body("succeful");
        } else {
            throw new NoQuestionsException("No Questions");
        }

    }

    public ResponseEntity<String> createFlashCard(List<Question> questions,String title){
        try {
            List<Integer> questionId = flashCardInterface.saveQuestions(questions).getBody();
            saveFlashCard(questionId, title);
            return ResponseEntity.ok().body("flash card created");
        }catch (Exception | NoQuestionsException e){
            throw new RuntimeException();
        }
    }
    public ResponseEntity<List<FlashCardGroup>> getAllFlashCard(){
        List<FlashCardGroup> flashCard = flashCardDao.findAll();
        return new ResponseEntity<>(flashCard,HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> deleteFlashCardQuestion(int qId){
        flashCardInterface.deleteQuestionById(qId);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<String> deleteFlashCard(int groupId) {
        flashCardDao.deleteById(groupId);
        return ResponseEntity.ok().body("deleted");
    }
    public FlashCardResponse getQuestionsByGroup(int groupId) throws FlashCardNotFoundException {
        FlashCardGroup flashCardGroup = flashCardDao.findById(groupId).orElseThrow(()->new FlashCardNotFoundException("flash card not found"));
        FlashCardDto flashCardDto = new FlashCardDto();
        flashCardDto.setId(flashCardGroup.getId());
        flashCardDto.setTitle(flashCardGroup.getTitle());
        List<Question> questionsList =flashCardInterface.getQuestionsByIds(flashCardGroup.getQuestionIds()).getBody();
        return createFlashCardResponse(flashCardDto,questionsList);
    }

    public ResponseEntity<HttpStatus> addQFlashCard(int groupId, List<Question> questions){
        FlashCardGroup flashCardGroup = flashCardDao.findById(groupId).orElseThrow(() -> new RuntimeException("Flashcard group not found"));
        List<Integer> newQuestionIds = flashCardInterface.saveQuestions(questions).getBody();
        List<Integer> existingQuestionIds = flashCardGroup.getQuestionIds();
        if (existingQuestionIds != null) {
            existingQuestionIds.addAll(newQuestionIds);
        } else {
            existingQuestionIds = new ArrayList<>(newQuestionIds);
        }
        flashCardGroup.setQuestionIds(existingQuestionIds);
        flashCardDao.save(flashCardGroup);
        return ResponseEntity.ok().build();
    }
    public static ResponseEntity<QuestionResponse> createQuestionResponse(List<Integer> questionsIds,List<Question> questions){
        QuestionResponse questionResponse = new QuestionResponse(questions);
        questionResponse.setQuestionsIds(questionsIds);
        questionResponse.setQuestions(questions);
        return ResponseEntity.ok().body(questionResponse);
    }

    public FlashCardResponse createFlashCardResponse(FlashCardDto flashCardDto, List<Question> questions){
        FlashCardResponse flashCardResponse = new FlashCardResponse();
        flashCardResponse.setFlashCardDto(flashCardDto);
        flashCardResponse.setQuestions(questions);
        return flashCardResponse;
    }


}
