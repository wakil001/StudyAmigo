package com.studypal.FlashCard.FlashCard;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlashCardConfig {
    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
