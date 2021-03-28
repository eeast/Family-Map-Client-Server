package model;

import java.util.ArrayList;

/**
 * Class for interpreting Json file containing a list of String surnames for FILL service
 */
public class Surnames {
    private ArrayList<String> data;

    /**
     * Constructor for Surnames class
     * @param data ArrayList of Strings containing surname data
     */
    public Surnames(ArrayList<String> data) {
        this.data = data;
    }

    /**
     * Getter for ArrayList of Strings containing surnames
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
