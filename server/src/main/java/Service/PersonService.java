package Service;

import data_access.*;
import model.Person;
import result.PersonResult;

import java.sql.Connection;

/**
 * Person lookup extension of the Service class. Contains the function for looking up a single Person object. A username
 * is also required, for confirmation of authorized access.
 */
public class PersonService extends Service {

    /**
     * Returns the single Person object with the specified ID.
     * @param id String id of the Person requested
     * @param username String username of the requesting user used for confirmation of authorized access
     * @return PersonResult object with a boolean success identifier, and if successful, the data from the Person
     * object requested. If unsuccessful, a String error message is included identifying the issue.
     */
    public PersonResult person(String id, String username) {
        try {
            Connection conn = db.getConnection();
            pDAO = new PersonDAO(conn);
            Person p = pDAO.getPerson(id);
            db.closeConnection(false);
            if (p != null) {
                if (p.getAssoc_username().equals(username)) {
                    PersonResult result = new PersonResult(p.getAssoc_username(), p.getPerson_id(), p.getFirst_name(),
                            p.getLast_name(), p.getGender());
                    if (p.getFather_id() != null) { result.setFather_id(p.getFather_id()); }
                    if (p.getMother_id() != null) { result.setMother_id(p.getMother_id()); }
                    if (p.getSpouse_id() != null) { result.setSpouse_id(p.getSpouse_id()); }
                    return result;
                } else {
                    return new PersonResult("ERROR: Requested person does not belong to this user");
                }
            } else {
                return new PersonResult("ERROR: Invalid personID number parameter");
            }
        } catch (Exception e) {
            try {
                db.closeConnection(false);
            } catch (Exception ex) {}
        }
        return new PersonResult("ERROR: Internal server error");
    }
}
