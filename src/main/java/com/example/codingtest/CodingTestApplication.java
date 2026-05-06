package com.example.codingtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CodingTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodingTestApplication.class, args);
	}

}
