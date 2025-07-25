package com.rinhabackend.antonio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableAsync
@EnableScheduling
public class RinhaBackendAntonioApplication {

	public static void main(String[] args) {
		SpringApplication.run(RinhaBackendAntonioApplication.class, args);
	}

}
