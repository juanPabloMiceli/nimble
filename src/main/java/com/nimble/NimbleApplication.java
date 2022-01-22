package com.nimble;

import com.nimble.model.server.TimePenalties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TimePenalties.class)
public class NimbleApplication {

	public static void main(String[] args) {
		SpringApplication.run(NimbleApplication.class, args);
	}
}
