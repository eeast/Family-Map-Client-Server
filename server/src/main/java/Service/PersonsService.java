package Service;

import data_access.PersonDAO;
import model.Person;
import result.PersonsResult;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Persons lookup extension of the Service class. Contains the function for looking up ALL Person objects associated with
 * a given username.
 */
public class PersonsService extends Service {

    /**
     * Returns ALL family members of the current user. The current user is determined from the provided auth token.
     * @param username String username of the user requesting Events data; used in query
     * @return PersonsResult object with a boolean success identifier, and if successful, a list of ALL Person objects
     * associated with the current User. If unsuccessful, a String error message is included identifying the issue.
     */
    public PersonsResult persons(String username) {
        try {
            Connection conn = db.getConnection();
            pDAO = new PersonDAO(conn);
            ArrayList<Person> people = pDAO.getPersons(username);
            db.closeConnection(false);
            return new PersonsResult(people);
        } catch (Exception e) {
            try {
                db.closeConnection(false);
            } catch (Exception ex) {}
        }
        return new PersonsResult("ERROR: Internal server error");
    }
}
