package org.example.logic.results;

public class Result<T> {

    private final T result;
    private final Exception exception;

    private Result(T result, Exception exception) {
        this.result = result;
        this.exception = exception;
    }

    public T Value() {
        return result;
    }
    public Exception Exception() {
        return exception;
    }
    public boolean Success() {
        return exception == null;
    }

    public boolean isExceptionType(Class<? extends Exception> exceptionClass) {
        return exceptionClass.isInstance(exception);
    }

    /**
     * Creates a new Result object with a successful result.
     *
     * @param result the result of the operation.
     * @param <T> the type of the result.
     * @return a Result object with the provided result and a null error.
     */
    public static <T> Result<T> Ok(T result) {
        return new Result<T>(result, null);
    }

    /**
     * Creates a new Result object with an error.
     *
     * @param exception the exception that caused the error.
     * @param <T> the type of the result.
     * @return a Result object with a null result and the provided exception as the error.
     */
    public static <T> Result<T> Fail(Exception exception) {
        return new Result<T>(null, exception);
    }
}
