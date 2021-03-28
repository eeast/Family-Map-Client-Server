package result;

import model.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Result Object representing the service.result of a service.request for multiple Events, to be sent to the client
 */
public class EventsResult {
    /**
     * A List of Event Objects requested
     */
    private ArrayList<Event> data;
    /**
     * An error message (only used if the service.request was unsuccessful)
     */
    private String message;
    /**
     * A boolean indicator of success (if true)
     */
    private final boolean success;

    /**
     * Constructor for EventsResult when the requested events were successfully retrieved from the database
     * @param data List of Event objects requested
     */
    public EventsResult(ArrayList<Event> data) {
        this.data = data;
        this.success = true;
    }

    /**
     * Constructor for EventsResult when unable to retrieve the requested events from the database
     * @param message String error message identifying the issue
     */
    public EventsResult(String message) {
        this.message = message;
        this.success = false;
    }

    public ArrayList<Event> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
