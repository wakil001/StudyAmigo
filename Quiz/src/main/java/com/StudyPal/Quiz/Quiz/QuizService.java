package com.StudyPal.Quiz.Quiz;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class QuizService {

    private final QuizInterface quizInterface;
    private final QuizDao quizDao;
    private final ChatModel chatModel;


    public QuizService(QuizInterface quizInterface, QuizDao quizDao, ChatModel chatModel){
        this.quizInterface = quizInterface;
        this.quizDao = quizDao;
        this.chatModel = chatModel;
    }

    public ResponseEntity<QuestionResponse> createQuizWithRandomQDC(int qNum, String difficulty, String category){
        try {
            List<Integer> questionIds = quizInterface. getRandomQDC(qNum,difficulty,category).getBody().stream().distinct().toList();
            List<Question> questions = generateOptionsForQuestions(quizInterface.getQuestionsByIds(questionIds).getBody());
            return createQuestionResponse(questionIds,questions);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<String> generateQuiz(String difficulty, int qNum, String description) {

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
            - Generate {qNum} quiz questions based on the topic: {description}
            - Set the difficulty to: {difficulty}
            - Set the category to: AiGen
            - Remove any * or # from the response
            - Return the response in the following JSON format:
            ```json
            
               
                        "question": "Generated question based on {description}",
                        "answer": "Generated answer based on {description}",
                        "option1: "Generate an appropriate option"
                        "option2: "Generate an appropriate option"
                        "option3: "Generate an appropriate option"
                        "category": "AiGen",
                        "difficulty": "{difficulty}"
                 
         
            ```
            Ensure the response contains exactly {qNum} quiz objects in the "quiz" array.
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
    public ResponseEntity<QuestionResponse> createQuizWithRandomQ(int qNum){
        try {
            List<Integer> questionIds = quizInterface.getRandomQuestions(qNum).getBody().stream().distinct().toList();
            List<Question> questions = generateOptionsForQuestions(quizInterface.getQuestionsByIds(questionIds).getBody());
            return createQuestionResponse(questionIds,questions);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        }
    }


    public ResponseEntity<QuestionResponse> createQuizWithRandomQAndDiff(int qNum, String difficulty) {
        try {
            List<Integer> questionIds = quizInterface.getRandomQuestionsByDiff(qNum, difficulty).getBody();

            if (questionIds == null || questionIds.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<Question> questions = generateOptionsForQuestions(quizInterface.getQuestionsByIds(questionIds).getBody());
            if (questions == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
            questions = generateOptionsForQuestions(questions);
            return createQuestionResponse(questionIds, questions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    public QuizResponse getQuestionsByGroup(int groupId){
        QuizGroup quizGroup = quizDao.findById(groupId).get();
        QuizDto quizDto = new QuizDto();
        quizDto.setId(quizGroup.getId());
        quizDto.setTitle(quizGroup.getTitle());
        List<Question> questionsList = quizInterface.getQuestionsByIds(quizGroup.getQuestionsId()).getBody();
        List<Question> questions =generateOptionsForQuestions(questionsList);
        return createQuizResponse(quizDto,questions);

    }
    public ResponseEntity<HttpStatus> updateQuestion(Question question,int questionId) {
        quizInterface.updateQuestion(question,questionId);
        return ResponseEntity.ok().build();
    }
    public ResponseEntity<String> saveQuiz(List<Integer> questionIds,String title) {
        if (questionIds != null) {
           QuizGroup quizGroup = new QuizGroup();
            quizGroup.setTitle(title);
            quizGroup.setQuestionsId(questionIds);
            quizDao.save(quizGroup);
            return ResponseEntity.status(HttpStatus.OK).body("succeful");
        } else {
            throw new RuntimeException("no questions to save");
        }
    }
    public ResponseEntity<List<Question>> createQuiz(List<Question> questions,String title){
        try {
            List<Integer> questionId = quizInterface.saveQuestions(questions).getBody();
            saveQuiz(questionId, title);
            return ResponseEntity.ok().body(questions);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }
    public ResponseEntity<HttpStatus> addQtoQuiz(int groupId, List<Question> questions){
        QuizGroup quizGroup = quizDao.findById(groupId).orElseThrow(() -> new RuntimeException("Quiz group not found"));
        List<Integer> newQuestionIds = quizInterface.saveQuestions(questions).getBody();
        List<Integer> existingQuestionIds = quizGroup.getQuestionsId();
        if (existingQuestionIds != null) {
            existingQuestionIds.addAll(newQuestionIds);
        } else {
            existingQuestionIds = new ArrayList<>(newQuestionIds);
        }
        quizGroup.setQuestionsId(existingQuestionIds);
        quizDao.save(quizGroup);
        return ResponseEntity.ok().build();
    }
    public ResponseEntity<HttpStatus> deleteQuizQuestion(int qNum){
        quizInterface.deleteQuestionById(qNum);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<String> deleteQuiz(int groupId) {
        quizDao.deleteById(groupId);
        return ResponseEntity.ok().body("deleted");
    }

    public ResponseEntity<List<QuizGroup>> getAllQuiz(){
        return new ResponseEntity<>(quizDao.findAll(),HttpStatus.OK);

    }



    public List<Question> generateOptionsForQuestions(List<Question> questions) {
        List<Question> listOfQuestions = new ArrayList<>();
        List<Question> questionList = quizInterface.getAllquestions().getBody();

        assert questionList != null;
        List<String> answers = questionList.stream()
                .map(Question::getAnswer)
                .filter(answer -> answer != null)
                .toList();

        if (answers.isEmpty()) {
            throw new RuntimeException("No valid answers available to generate options.");
        }

        Random random = new Random();

        for (Question question : questions) {
            if (question.getOption1() == null || question.getOption2() == null || question.getOption3() == null) {

                question.setOption1(answers.get(random.nextInt(answers.size())));
                question.setOption2(answers.get(random.nextInt(answers.size())));
                question.setOption3(answers.get(random.nextInt(answers.size())));

                while (question.getOption1().equals(question.getOption2())) {
                    question.setOption2(answers.get(random.nextInt(answers.size())));
                }
                while (question.getOption1().equals(question.getOption3()) || question.getOption2().equals(question.getOption3())) {
                    question.setOption3(answers.get(random.nextInt(answers.size())));
                }
            }
            listOfQuestions.add(question);
        }
        return listOfQuestions;
    }
    public ResponseEntity<QuestionResponse> createQuestionResponse(List<Integer> questionsIds,List<Question> questions){
        QuestionResponse questionResponse = new QuestionResponse();
        questionResponse.setQuestionsIds(questionsIds);
        questionResponse.setQuestions(questions);
        return ResponseEntity.ok().body(questionResponse);
    }

    public QuizResponse createQuizResponse(QuizDto quizDto, List<Question> questions){
        QuizResponse quizResponse = new QuizResponse();
        quizResponse.setQuizDto(quizDto);
        quizResponse.setQuestions(questions);
        return quizResponse;
    }


}
