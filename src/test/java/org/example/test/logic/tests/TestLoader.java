package org.example.test.logic.tests;

import org.example.fronskyframework.logic.results.Result;
import org.example.test.logic.interfaces.ITest;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class TestLoader {

    private final Map<Class<? extends ITest>, ITest> tests = new HashMap<>();

    /**
     * The prepare method is used to store a test in a map.
     * If the test is not already in the map, it will be added.
     *
     * @param test The test to be stored in the map
     * @throws NullPointerException if the test is null
     */
    public void prepare(@Nonnull ITest test) {
        if (!tests.containsKey(test.getClass())) {
            tests.put(test.getClass(), test);
        }
    }

    /**
     * The executeTests method is used to run all the tests stored in the map.
     * If a test fails, the exception message will be printed.
     *
     * @throws NullPointerException if the test or result is null
     */
    public void executeTests() {
        for(ITest test : tests.values()) {
            Result<String> result = test.Tests();
            if (!result.Success()) {
                System.out.println(result.Exception().getMessage());
            }
        }
    }
}
