package result;

/**
 * Result Object representing the service.result of a service.request to clear the data, to be sent to the client
 */
public class ClearResult {
    /**
     * Feedback message, either confirming completion or identifying the issue
     */
    private final String message;
    /**
     * A boolean indicator of success (if true)
     */
    private final boolean success;

    /**
     * Constructor for ClearResult object with a message identifying the results of the service.request
     * @param m String message identifying that the requested clear was successful, of if unsuccessful, an error
     *          message identifying the issue.
     * @param s boolean success indicator, with true indicating the successful completion of the service.request
     */
    public ClearResult(String m, boolean s) {
        this.message = m;
        this.success = s;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
