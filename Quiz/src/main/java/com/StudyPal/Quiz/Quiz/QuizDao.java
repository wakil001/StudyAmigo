package com.StudyPal.Quiz.Quiz;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizDao extends JpaRepository<QuizGroup,Integer> {
}
