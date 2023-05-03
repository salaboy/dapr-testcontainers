package com.salaboy.dapr.javaapp;

import org.testcontainers.containers.GenericContainer;

public class DaprPlacementContainer extends GenericContainer<DaprPlacementContainer> {

    DaprPlacementContainer() {
        super("daprio/dapr");
        withCommand("./placement", "-port", "50006");
        withExposedPorts(50006);
    }
}
