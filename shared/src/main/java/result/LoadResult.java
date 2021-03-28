package result;

/**
 * Result Object representing the service.result of a LOAD service.request, to be sent to the client
 */
public class LoadResult {
    /**
     * Feedback message, either confirming completion or identifying the issue
     */
    private final String message;
    /**
     * A boolean indicator of success (if true)
     */
    private final boolean success;

    /**
     * Constructor for LoadResult object with a message identifying the results of the Load service.request
     * @param m String message indicating the service.request was completed, or if unsuccessful, an error message
     *          identifying the issue
     * @param s boolean success indicator, with true indicating the successful completion of the service.request
     */
    public LoadResult(String m, boolean s) {
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
