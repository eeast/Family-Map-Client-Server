package model;

/**
 * Class for interpreting Json file containing location data with four elements:
 * String country
 * String city
 * float latitude
 * float longitude
 */
public class Location {
    /**
     * String variable identifying the country
     */
    private String country;
    /**
     * String variable identifying the city
     */
    private String city;
    /**
     * float variable identifying the latitude
     */
    private float latitude;
    /**
     * float variable identifying the longitude
     */
    private float longitude;

    /**
     * Constructor for creating a single location with given parameters
     * @param country
     * @param city
     * @param latitude
     * @param longitude
     */
    public Location(String country, String city, float latitude, float longitude) {
        this.country = country;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Getter for private variable country
     * @return String country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Getter for private variable city
     * @return String city
     */
    public String getCity() {
        return city;
    }

    /**
     * Getter for private variable latitude
     * @return float latitude
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * Getter for private variable longitude
     * @return float longitude
     */
    public float getLongitude() {
        return longitude;
    }
}
