package com.studypal.FlashCard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FlashCardApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlashCardApplication.class, args);
	}

}
