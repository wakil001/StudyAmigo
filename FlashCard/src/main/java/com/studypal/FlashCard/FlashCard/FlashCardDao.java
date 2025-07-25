package com.studypal.FlashCard.FlashCard;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FlashCardDao extends JpaRepository<FlashCardGroup,Integer> {
}
