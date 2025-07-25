package com.studypal.Questions.Question;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@Service
public class QuestionService {

    private final QuestionDao questionDao;
    public QuestionService(QuestionDao questionDao){
        this.questionDao = questionDao;
    }

    public ResponseEntity<List<Integer>> getRandomQDC(int qNum, String difficulty, String category) {
        try{
            List<Integer> questionsIds = questionDao.getRandomQDC(qNum,difficulty,category);
            return new ResponseEntity<>(questionsIds, HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<List<Question>> getQuestionsByIds(List<Integer> questionIds) {
        try{
            List<Question> questions = questionDao.findByIdIn(questionIds);
            return new ResponseEntity<>(questions,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<List<Integer>> getRandomQ(int qNum) {
        try{
            List<Integer> questionsIds = questionDao.getRandomQ(qNum);
            return new ResponseEntity<>(questionsIds,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<List<Integer>> getRandomQD(int qNum, String difficulty) {
        try{
            List<Integer> questionsIds = questionDao.getRandomQD(qNum,difficulty);
            return new ResponseEntity<>(questionsIds,HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<List<Integer>> saveQuestions(List<Question> questions) {
        try{
            List<Question> savedQuestions = questionDao.saveAll(questions);
            List<Integer> questionidList = savedQuestions.stream()
                    .map(Question::getId)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(questionidList,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<List<Question>> getAllquestions() {
        List<Question> questions = questionDao.findAll();
        return new ResponseEntity<>(questions,HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> deleteQuestions(List<Integer> ids) {
         questionDao.deleteAllById(ids);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<HttpStatus> updateQuestion(int questionId,Question updatedQuestion) {
        Optional<Question> existingQuestionOpt = questionDao.findById(questionId);
        if (existingQuestionOpt.isPresent()) {
            Question existingQuestion = existingQuestionOpt.get();

            existingQuestion.setQuestion(updatedQuestion.getQuestion());
            existingQuestion.setAnswer(updatedQuestion.getAnswer());
            existingQuestion.setDifficulty(updatedQuestion.getDifficulty());
            questionDao.save(existingQuestion);
            return ResponseEntity.ok().build();
        } else {
            throw new RuntimeException("Question not found with ID: " + questionId);
        }
    }
    public ResponseEntity<HttpStatus> deleteQuestionById(int qId){
        questionDao.deleteById(qId);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<HttpStatus> deleteAllQuestions() {
        questionDao.deleteAll();
        return ResponseEntity.ok().build();
    }
}

