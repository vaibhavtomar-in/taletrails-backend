package com.taletrails.taletrails_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class TaletrailsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaletrailsBackendApplication.class, args);
	}

}
