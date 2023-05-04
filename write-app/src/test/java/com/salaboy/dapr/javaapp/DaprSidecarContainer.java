package com.salaboy.dapr.javaapp;

import org.testcontainers.containers.GenericContainer;

public class DaprSidecarContainer extends GenericContainer<DaprSidecarContainer> {

    DaprSidecarContainer() {
        super("daprio/daprd:edge");
        withCommand("./daprd",
                "-app-id", "write-app",
                "-placement-host-address", "placement:50006",
                "--dapr-listen-addresses=0.0.0.0",
                "-components-path", "/components");
        withExposedPorts(50001);
    }
}
