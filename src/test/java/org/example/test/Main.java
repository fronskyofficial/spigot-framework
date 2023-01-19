package org.example.test;

import org.example.test.logic.tests.TestLoader;

public class Main {

    private final TestLoader testLoader = new TestLoader();

    public void onEnable() {
//        testLoader.prepare(new FirstTest());
        testLoader.executeTests();
        System.out.println("Hello World!");
    }
}
