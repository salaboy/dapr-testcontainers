package com.salaboy.dapr.javaapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class JavaAppApplication {

	private static final Logger log = LoggerFactory.getLogger(JavaAppApplication.class);

	private List<String> notifications = new ArrayList<String>();
	public static void main(String[] args) {
		SpringApplication.run(JavaAppApplication.class, args);
	}

	@PostMapping("/notifications")
	public void receiveNotifications(@RequestBody Notificaiton notification ) {
		System.out.println("Message Received: " + notification.data);
		notifications.add(notification.data);
	}

	@GetMapping("/notifications")
	public List<String> getNotifications() {
		return notifications;
	}

	
	public record Notificaiton(String data) {}
}



