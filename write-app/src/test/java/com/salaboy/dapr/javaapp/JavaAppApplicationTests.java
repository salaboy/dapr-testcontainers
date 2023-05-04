package com.salaboy.dapr.javaapp;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.utility.MountableFile;

import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JavaAppApplicationTests {

    @LocalServerPort
    private int localPort;

    private static Network daprNetwork = Network.newNetwork();

    private static final GenericContainer<?> daprSidecar = new GenericContainer<>("daprio/daprd:edge")
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

    private static final GenericContainer<?> daprPlacement = new GenericContainer<>("daprio/dapr")
            .withCommand("./placement", "-port", "50006")
            .withExposedPorts(50006) // for wait
            .withNetwork(daprNetwork)
            .withNetworkAliases("placement");

    private static final GenericContainer<?> redis = new GenericContainer<>("redis:alpine")
            .withExposedPorts(6379) // for wait
            .withNetwork(daprNetwork)
            .withNetworkAliases("redis");

    @DynamicPropertySource
    static void daprProperties(DynamicPropertyRegistry registry) {
        Stream.of(daprSidecar, daprPlacement, redis).parallel().forEach(GenericContainer::start);
        System.setProperty("dapr.grpc.port", daprSidecar.getMappedPort(50001).toString());
    }

    @Test
    void contextLoads() {
    }

    @Test
    void canPostToEndpoint() {
        RestAssured.given().port(localPort).param("value", "foo")
                .when().post("/")
                .then().statusCode(200);

        RestAssured.given().port(localPort).param("value", "bar")
                .when().post("/")
                .then()
                .statusCode(200)
                .body("values", Matchers.contains("foo", "bar"));
    }
}
