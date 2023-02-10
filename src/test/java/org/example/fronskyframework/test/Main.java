package org.example.fronskyframework.test;

import org.example.fronskyframework.test.logic.tests.TestLoader;

public class Main {

    private final TestLoader testLoader = new TestLoader();

    public void onEnable() {
        testLoader.executeTests();
        System.out.println("Hello World!");
    }
}
