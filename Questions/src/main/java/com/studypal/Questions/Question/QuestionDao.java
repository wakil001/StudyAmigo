package com.studypal.Questions.Question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionDao extends JpaRepository<Question,Integer> {

    List<Question> findByIdIn(List<Integer> questionIds);

    @Query(nativeQuery = true,value="SELECT questions.id FROM questions WHERE questions.difficulty = :difficulty AND category = :category ORDER BY RANDOM() LIMIT :qNum")
    List<Integer> getRandomQDC(@Param("qNum") int qNum, @Param("difficulty") String difficulty,@Param("category") String category);

    @Query(nativeQuery = true,value="SELECT questions.id FROM questions ORDER BY RANDOM() LIMIT :qNum")
    List<Integer> getRandomQ(@Param("qNum") int qNum);

    @Query(nativeQuery = true,value="SELECT questions.id FROM questions WHERE questions.difficulty = :difficulty ORDER BY RANDOM() LIMIT :qNum")
    List<Integer> getRandomQD(@Param("qNum") int qNum,@Param("difficulty") String difficulty);


    <S extends Question> List<S> saveAll(Iterable<S> entities);
}
