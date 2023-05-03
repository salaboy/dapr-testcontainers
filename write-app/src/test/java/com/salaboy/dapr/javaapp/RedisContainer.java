package com.salaboy.dapr.javaapp;

import org.testcontainers.containers.GenericContainer;

public class RedisContainer extends GenericContainer<RedisContainer> {

    RedisContainer() {
        super("redis:alpine");
        withExposedPorts(6379);
    }
}
