package org.example.fronskyspigotframework.test;

import org.example.fronskyspigotframework.test.logic.logger.TestLogger;
import org.example.fronskyspigotframework.test.logic.tests.TestLoader;

public class Main {

    private final TestLoader testLoader = new TestLoader();
    private final TestLogger testLogger = new TestLogger(this.getClass());

    public void onEnable() {
        testLoader.executeTests();
        testLogger.logInfo("Hello World!");
    }
}
