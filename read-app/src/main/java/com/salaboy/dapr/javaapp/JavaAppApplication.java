package com.salaboy.dapr.javaapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.dapr.client.domain.State;


import java.util.List;

@SpringBootApplication
@RestController
public class JavaAppApplication {

	private static final Logger log = LoggerFactory.getLogger(JavaAppApplication.class);

	@Value("${STATE_STORE_NAME:statestore}")
	private String STATE_STORE_NAME = "";

	private DaprClient client = new DaprClientBuilder().build();

	public static void main(String[] args) {
		SpringApplication.run(JavaAppApplication.class, args);

	}

	@GetMapping("/")
	public MyValues readValues() {
		State<MyValues> results = client.getState(STATE_STORE_NAME, "values", MyValues.class).block();
		
		return results.getValue();
	}

	
	public record MyValues(List<String> values) {}

}


