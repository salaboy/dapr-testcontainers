///usr/bin/env jbang "$0" "$@" ; exit $?

//REPOS mavencentral

//DEPS org.apache.maven.shared:maven-invoker:3.2.0
//DEPS org.testcontainers:testcontainers:1.18.0

//JAVA 17

import java.io.File;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.utility.MountableFile;

public class Run {

    public static void main(String... args) throws Exception {
        var reuse = false;

        Network daprNetwork = Network.newNetwork();

        GenericContainer<?> redis = new GenericContainer<>("redis:alpine")
                .withExposedPorts(6379) // for wait
                .withNetwork(daprNetwork)
                .withNetworkAliases("redis")
                .withReuse(reuse);
        redis.start();

        GenericContainer<?> daprPlacement = new GenericContainer<>("daprio/dapr")
                .withCommand("./placement", "-port", "50006")
                .withExposedPorts(50006) // for wait
                .withNetwork(daprNetwork)
                .withNetworkAliases("placement")
                .withReuse(reuse);
        daprPlacement.start();

        GenericContainer<?> readAppDaprSidecar = new GenericContainer<>("daprio/daprd:edge")
                .withCommand("./daprd",
                        "-app-id", "read-app",
                        "-placement-host-address", "placement:50006",
                        "--dapr-listen-addresses=0.0.0.0",
                        "-components-path", "/components")
                .withExposedPorts(50001)
                .withCopyFileToContainer(
                        MountableFile.forHostPath("./components"),
                        "/components/")
                .withNetwork(daprNetwork)
                .withReuse(reuse);
        readAppDaprSidecar.start();

        GenericContainer<?> writeAppDaprSidecar = new GenericContainer<>("daprio/daprd:edge")
                .withCommand("./daprd",
                        "-app-id", "write-app",
                        "-placement-host-address", "placement:50006",
                        "--dapr-listen-addresses=0.0.0.0",
                        "-components-path", "/components")
                .withExposedPorts(50001)
                .withCopyFileToContainer(
                        MountableFile.forHostPath("./components"),
                        "/components/")
                .withNetwork(daprNetwork)
                .withReuse(reuse);
        writeAppDaprSidecar.start();

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        var readAppSidecarPort = readAppDaprSidecar.getMappedPort(50001);
        var readAppRequest = buildRequest("read-app/pom.xml", readAppSidecarPort);

        var readAppInvoker = new DefaultInvoker();
        Future<InvocationResult> future1 = executorService.submit(() -> readAppInvoker.execute(readAppRequest));

        var writeAppSidecarPort = writeAppDaprSidecar.getMappedPort(50001);
        var writeAppRequest = buildRequest("write-app/pom.xml", writeAppSidecarPort, 8081);

        Invoker writeAppInvoker = new DefaultInvoker();
        Future<InvocationResult> future2 = executorService.submit(() -> writeAppInvoker.execute(writeAppRequest));

        future1.get();
        future2.get();
    }

    private static InvocationRequest buildRequest(String pom, int sidecarPort) {
        return buildRequest(pom, sidecarPort, 8080);
    }

    private static InvocationRequest buildRequest(String pom, int sidecarPort, int serverPort) {
        var mavenHome = new File(System.getenv("MAVEN_HOME"));
        var javaHome = new File(System.getenv("JAVA_HOME"));

        var request = new DefaultInvocationRequest();
        request.setPomFile(new File(pom));
        request.setMavenHome(mavenHome);
        request.setJavaHome(javaHome);
        request.setGoals(Collections.singletonList("spring-boot:run"));
        request.addShellEnvironment("DAPR_GRPC_PORT", String.valueOf(sidecarPort));
        request.addShellEnvironment("SERVER_PORT", String.valueOf(serverPort));
        return request;
    }

}
