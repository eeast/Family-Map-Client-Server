package Service;

import result.ClearResult;

/**
 * Clearing extension of the Service class. Contains the function for clearing all data from the database.
 */
public class ClearService extends Service {

    /**
     * Deletes ALL data from the database, including user accounts, auth tokens, and generated person and event data.
     * @return ClearResult object with a boolean success identifier, and a String message.
     */
    public ClearResult clearTables() {
        try {
            db.getConnection();
            db.clearTables();
            db.closeConnection(true);
            return new ClearResult("Clear succeeded.", true);
        } catch (Exception e) {
            try {
                db.closeConnection(false);
            } catch (Exception ex) {}
        }
        return new ClearResult("ERROR: Internal server error", false);
    }
}
