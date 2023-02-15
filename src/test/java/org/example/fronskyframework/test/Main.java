package org.example.fronskyframework.test;

import org.example.fronskyframework.test.logic.logger.TestLogger;
import org.example.fronskyframework.test.logic.tests.TestLoader;

public class Main {

    private final TestLoader testLoader = new TestLoader();
    private final TestLogger testLogger = new TestLogger(this.getClass());

    public void onEnable() {
        testLoader.executeTests();
        testLogger.logInfo("Hello World!");
    }
}
