package model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Model Object representing an Event
 */
public class Event {
    /**
     * A UNIQUE id for identifying the Event
     */
    @SerializedName("eventID")
    private final String event_id;
    /**
     * The username of the associated User
     */
    @SerializedName("associatedUsername")
    private final String assoc_username;
    /**
     * The person_id of the associated Person
     */
    @SerializedName("personID")
    private final String person_id;
    /**
     * float representation of the latitude for the Event
     */
    private final float latitude;
    /**
     * float representation of the longitude for the Event
     */
    private final float longitude;
    /**
     * The country in which the Event occurred
     */
    private final String country;
    /**
     * The city in which the Event occurred
     */
    private final String city;
    /**
     * A description of the Event
     */
    @SerializedName("eventType")
    private final String event_type;
    /**
     * The year in which the Event occurred
     */
    private final int year;

    /**
     * Constructor for Event object with predefined parameters
     * @param event_id String unique event id
     * @param assoc_username String username of the user associated with the person identified in person_id
     * @param person_id String person id of the person associated with the event identified in event_id
     * @param latitude float latitude position (for use with map API)
     * @param longitude float longitude position (for use with map API)
     * @param country String name of country event occurred in
     * @param city String name of city event occurred in
     * @param event_type String description of the event
     * @param year int year the event occurred in
     */
    public Event(String event_id, String assoc_username, String person_id, float latitude, float longitude,
                 String country, String city, String event_type, int year) {
        this.event_id = event_id;
        this.assoc_username = assoc_username;
        this.person_id = person_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.event_type = event_type;
        this.year = year;
    }

    public String getEvent_id() {
        return event_id;
    }

    public String getAssoc_username() {
        return assoc_username;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Float.compare(event.latitude, latitude) == 0 && Float.compare(event.longitude, longitude) == 0 && year == event.year && event_id.equals(event.event_id) && assoc_username.equals(event.assoc_username) && person_id.equals(event.person_id) && country.equals(event.country) && city.equals(event.city) && event_type.equals(event.event_type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event_id, assoc_username, person_id, latitude, longitude, country, city, event_type, year);
    }

    public boolean isValid() {
        return event_id != null && assoc_username != null && person_id != null && latitude != 0 && longitude != 0 &&
                country != null && city != null && year != 0;
    }
}
