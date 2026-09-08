package com.projects.shift_scheduler_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShiftSchedulerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShiftSchedulerApiApplication.class, args);
	}

}
