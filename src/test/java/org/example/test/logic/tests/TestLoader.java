package org.example.test.logic.tests;

import org.example.logic.results.Result;
import org.example.test.logic.interfaces.ITest;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class TestLoader {

    private final Map<Class<? extends ITest>, ITest> tests = new HashMap<>();

    public void prepare(@Nonnull ITest test) {
        if (! tests.containsKey(test.getClass())) {
            tests.put(test.getClass(), test);
        }
    }

    public void executeTests() {
        for(ITest test : tests.values()) {
            Result<String> result = test.Tests();
            if (! result.Success()) {
                System.out.println(result.Exception().getMessage());
            }
        }
    }
}
