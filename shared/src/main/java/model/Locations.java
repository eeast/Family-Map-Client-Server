package model;

import java.util.ArrayList;

/**
 * Class for interpreting Json file containing a list of String surnames for FILL service
 */
public class Locations {
    ArrayList<Location> data;

    /**
     * Constructor for Locations class with a given ArrayList of Location objects
     * @param data ArrayList of Location objects
     */
    public Locations(ArrayList<Location> data) {
        this.data = data;
    }

    /**
     * Getter for ArrayList of String surnames
     * @return ArrayList of Strings data
     */
    public ArrayList<Location> getData() {
        return data;
    }

    /**
     * Passthrough of size() function for private ArrayList variable
     * @return int data.size()
     */
    public int size() {
        return data.size();
    }
}
