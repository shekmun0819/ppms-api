package com.example.ppms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PpmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PpmsApplication.class, args);
	}
}
