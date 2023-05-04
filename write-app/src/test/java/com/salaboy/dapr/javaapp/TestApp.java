package com.salaboy.dapr.javaapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.utility.MountableFile;

import java.util.stream.Stream;

public class TestApp {

    public static void main(String[] args) {
        SpringApplication.from(JavaAppApplication::main)
                .with(DaprTestConfiguration.class)
                .run(args);
    }

    @ImportTestcontainers
    static class DaprTestConfiguration {

        static Network daprNetwork = Network.newNetwork();

        static RedisContainer redis = new RedisContainer()
                .withNetwork(daprNetwork)
                .withNetworkAliases("redis");

        static DaprSidecarContainer daprSidecar = new DaprSidecarContainer()
                .withCopyFileToContainer(
                        MountableFile.forClasspathResource("components"),
                        "/components/")
                .withNetwork(daprNetwork);

        static DaprPlacementContainer daprPlacement = new DaprPlacementContainer()
                .withNetwork(daprNetwork)
                .withNetworkAliases("placement");

        @DynamicPropertySource
        static void daprProperties(DynamicPropertyRegistry registry) {
            Stream.of(daprSidecar, daprPlacement, redis).parallel().forEach(GenericContainer::start);
            System.setProperty("dapr.grpc.port", daprSidecar.getMappedPort(50001).toString());
        }

    }

}
