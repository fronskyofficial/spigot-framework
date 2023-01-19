package org.example.test.logic.interfaces;

import org.example.logic.results.Result;

public interface ITest {

    /**
     * Runs test cases and return the test results
     *
     * @return The test results
     */
    public Result<String> Tests();
}
