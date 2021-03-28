package model;

import java.util.ArrayList;

/**
 * Class for interpreting Json file containing a list of String surnames for FILL service
 */
public class MaleNames {
    ArrayList<String> data;

    /**
     * Constructor for MaleNames class
     * @param data ArrayList of Strings containing male name data
     */
    public MaleNames(ArrayList<String> data) {
        this.data = data;
    }

    /**
     * Getter for ArrayList of Strings containing male names
     * @return ArrayList of Strings data
     */
    public ArrayList<String> getData() {
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
