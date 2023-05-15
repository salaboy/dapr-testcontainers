package com.salaboy.dapr.javaapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.dapr.client.domain.State;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class JavaAppApplication {

	private static final Logger log = LoggerFactory.getLogger(JavaAppApplication.class);

	private MyValues values = new MyValues(new ArrayList<String>());
	public static void main(String[] args) {
		SpringApplication.run(JavaAppApplication.class, args);
	}

	@PostMapping("/notifications")
	public void receiveNotifications(@RequestBody String message ) {
		System.out.println("Message Received: " + message);
		values.values().add(message);
	}

	@GetMapping("/")
	public MyValues getNotifications() {
		return values;
	}

	public record MyValues(List<String> values) {}
}



