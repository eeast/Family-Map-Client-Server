package result;

import com.google.gson.annotations.SerializedName;

/**
 * Result Object representing the service.result of a service.request for an Event, to be sent to the client
 */
public class EventResult {
    /**
     * The username of the associated User
     */
    @SerializedName("associatedUsername")
    private String assoc_username;
    /**
     * A UNIQUE id for identifying the Event
     */
    @SerializedName("eventID")
    private String event_id;
    /**
     * The person_id of the associated Person
     */
    @SerializedName("personID")
    private String person_id;
    /**
     * float representation of the latitude for the Event
     */
    float latitude;
    /**
     * float representation of the longitude for the Event
     */
    float longitude;
    /**
     * The country in which the Event occurred
     */
    String country;
    /**
     * The city in which the Event occurred
     */
    String city;
    /**
     * A description of the Event
     */
    @SerializedName("eventType")
    String event_type;
    /**
     * The year in which the Event occurred
     */
    int year;
    /**
     * An error message (only used if the service.request was unsuccessful)
     */
    private String message;
    /**
     * A boolean indicator of success (if true)
     */
    private final boolean success;

    /**
     * Constructor for EventResult object when an event was successfully retrieved from the database
     * @param assoc_username String username of the User associated with the person identified in person_id
     * @param event_id String unique event id
     * @param person_id String person id of the Person associated with the event identified in event_id
     * @param latitude float latitude position (for use with map API)
     * @param longitude float longitude position (for use with map API)
     * @param country String name of country event occurred in
     * @param city String name of city event occurred in
     * @param event_type String description of the event
     * @param year int year the event occurred in
     */
    public EventResult(String assoc_username, String event_id, String person_id, float latitude, float longitude,
            String country, String city, String event_type, int year) {
        this.assoc_username = assoc_username;
        this.event_id = event_id;
        this.person_id = person_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.year = year;
        this.event_type = event_type;
        this.success = true;
    }

    /**
     * Constructor for EventResult object when unable to retrieve an event from the database
     * @param message - String error message detailing the issue
     */
    public EventResult(String message) {
        this.message = message;
        this.success = false;
    }

    public String getAssoc_username() {
        return assoc_username;
    }

    public String getEvent_id() {
        return event_id;
    }

    public String getPerson_id() {
        return person_id;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEvent_type() {
        return event_type;
    }

    public int getYear() {
        return year;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
