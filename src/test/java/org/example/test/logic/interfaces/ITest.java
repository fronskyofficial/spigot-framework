package org.example.test.logic.interfaces;

import org.example.fronskyframework.logic.results.Result;

public interface ITest {

    /**
     * Runs test cases and return the test results
     *
     * @return Result<String> object containing the result of the test and any exception message if the test failed.
     */
    public Result<String> Tests();
}
