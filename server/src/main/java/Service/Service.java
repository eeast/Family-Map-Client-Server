package Service;

import data_access.*;

/**
 * Template class for all Service classes containing the required DAO variables for performing functions on the Database
 */
public class Service {
    /**
     * Initial Database for establishing the connection to the database file
     */
    protected Database db = new Database();
    /**
     * Data Access Object for Authorization Token Objects
     */
    protected AuthTokenDAO atDAO;
    /**
     * Data Access Object for Event Objects
     */
    protected EventDAO eDAO;
    /**
     * Data Access Object for Person Objects
     */
    protected PersonDAO pDAO;
    /**
     * Data Access Object for User Objects
     */
    protected UserDAO uDAO;
}
