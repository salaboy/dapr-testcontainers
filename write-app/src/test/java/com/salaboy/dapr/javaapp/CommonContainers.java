package com.salaboy.dapr.javaapp;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.utility.MountableFile;

public interface CommonContainers {

    Network daprNetwork = Network.newNetwork();

    GenericContainer<?> redis = new GenericContainer<>("redis:alpine")
            .withExposedPorts(6379) // for wait
            .withNetwork(daprNetwork)
            .withNetworkAliases("redis");

    GenericContainer<?> daprSidecar = new GenericContainer<>("daprio/daprd:edge")
            .withCommand("./daprd",
                    "-app-id", "write-app",
                    "-placement-host-address", "placement:50006",
                    "--dapr-listen-addresses=0.0.0.0",
                    "-components-path", "/components")
            .withExposedPorts(50001)
            .withCopyFileToContainer(
                    MountableFile.forClasspathResource("components"),
                    "/components/")
            .withNetwork(daprNetwork);

    GenericContainer<?> daprPlacement = new GenericContainer<>("daprio/dapr")
            .withCommand("./placement", "-port", "50006")
            .withExposedPorts(50006) // for wait
            .withNetwork(daprNetwork)
            .withNetworkAliases("placement");

    @DynamicPropertySource
    static void daprProperties(DynamicPropertyRegistry registry) {
        System.setProperty("dapr.grpc.port", daprSidecar.getMappedPort(50001).toString());
    }

}
