package com.worlddance.ms_scoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsScoringApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsScoringApplication.class, args);
	}

}
