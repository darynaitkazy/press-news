package com.example.pressnews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class PressNewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PressNewsApplication.class, args);
	}

}
