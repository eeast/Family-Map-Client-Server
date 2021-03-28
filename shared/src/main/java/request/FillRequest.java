package request;

/**
 * Request Object for the receipt of FILL requests from the client
 */
public class FillRequest {
    /**
     * The UNIQUE username for the User
     */
    private final String username;
    /**
     * The number of generations of family information to populate (default: 4)
     */
    private int generations;

    /**
     * Constructor for FillRequest object when a number of generations is specified
     * @param username String username of the user to populate family information for
     * @param generations int number of generations to populate
     */
    public FillRequest(String username, int generations) {
        this.username = username;
        this.generations = generations;
    }

    /**
     * Constructor for FillRequest object when a number of generations is NOT specified
     * @param username String username of the user to populate family information for
     */
    public FillRequest(String username) {
        this.username = username;
        this.generations = 4;
    }

    public String getUsername() {
        return username;
    }

    public int getGenerations() {
        return generations;
    }
}
