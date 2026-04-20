package com.nerosoft.linkyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LinkyouApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinkyouApplication.class, args);
	}

}
