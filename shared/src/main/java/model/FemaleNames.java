package model;

import java.util.ArrayList;

/**
 * Class for interpreting Json file containing a list of String Female Names for FILL service
 */
public class FemaleNames {
    ArrayList<String> data;

    /**
     * Constructor for FemaleNames class
     * @param data ArrayList of Strings containing female name data
     */
    public FemaleNames(ArrayList<String> data) {
        this.data = data;
    }

    /**
     * Getter for ArrayList of Strings containing female names
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
