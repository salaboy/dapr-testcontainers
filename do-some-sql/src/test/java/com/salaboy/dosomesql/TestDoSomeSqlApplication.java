package com.salaboy.dosomesql;

import org.springframework.boot.SpringApplication;

public class TestDoSomeSqlApplication {
  
  public static void main(String[] args) {
    SpringApplication.from(DoSomeSqlApplication::main)
      .with(TestEnvironmentConfiguration.class)
      .run(args);
  }
}

