package com.salaboy.dosomesql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DoSomeSqlApplication {

	private static final Logger log = LoggerFactory.getLogger(DoSomeSqlApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DoSomeSqlApplication.class, args);
	}


}

