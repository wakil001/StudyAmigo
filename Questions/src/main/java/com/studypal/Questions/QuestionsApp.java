package com.studypal.Questions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class QuestionsApp {

	public static void main(String[] args) {
		SpringApplication.run(QuestionsApp.class, args);
	}

}
