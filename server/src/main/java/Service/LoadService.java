package Service;

import data_access.*;
import model.Event;
import model.Person;
import model.User;
import request.LoadRequest;
import result.LoadResult;

import java.sql.Connection;

/**
 * Load extension of the Service class. Contains the function for loading a prepopulated set of family history data from
 * Lists of User, Person, and Event objects in the LoadRequest object.
 */
public class LoadService extends Service {

    /**
     * Clears all data from the database (just like the /clear API), and then loads the posted user, person,
     * and event data into the database.
     * @param r the LoadRequest object with the required parameters (list of users, list of persons, list of events)
     * @return LoadResult object with a boolean success identifier, and if successful, a String message identifying
     * the number of Users, Persons, and Events added to the database. If unsuccessful, a String error message
     * is included identifying the issue.
     */
    public LoadResult load(LoadRequest r) {
        try {
            Connection conn;
            conn = db.getConnection();
            db.clearTables();
            for (User u : r.getUsers()) {
                if (u.isValid()) {
                    uDAO = new UserDAO(conn);
                    User test = uDAO.getUser(u.getUsername());
                    if (test == null) {
                        uDAO.insertUser(u);
                    } else {
                        db.closeConnection(false);
                        return new LoadResult("ERROR: Username already taken by another user", false);
                    }
                } else {
                    db.closeConnection(false);
                    return new LoadResult("ERROR: User Object missing or has invalid value", false);
                }
            }
            for (Person p : r.getPersons()) {
                if (p.isValid()) {
                    pDAO = new PersonDAO(conn);
                    Person test = pDAO.getPerson(p.getPerson_id());
                    if (test == null) {
                        pDAO.insertPerson(p);
                    } else {
                        db.closeConnection(false);
                        return new LoadResult("ERROR: Duplicate Person ID", false);
                    }
                } else {
                    db.closeConnection(false);
                    return new LoadResult("ERROR: Person Object missing or has invalid value", false);
                }
            }
            for (Event e : r.getEvents()) {
                if (e.isValid()) {
                    eDAO = new EventDAO(conn);
                    Event test = eDAO.getEvent(e.getEvent_id());
                    if (test == null) {
                        eDAO.insertEvent(e);
                    } else {
                        db.closeConnection(false);
                        return new LoadResult("ERROR: Duplicate Event ID", false);
                    }
                } else {
                    db.closeConnection(false);
                    return new LoadResult("ERROR: Event Object missing or has invalid value", false);
                }
            }
            db.closeConnection(true);
            return new LoadResult("Successfully added " + r.getUsers().size() + " users, " + r.getPersons().size() +
                    " persons, and " + r.getEvents().size() + " events to the database.", true);
        } catch (Exception e) {
            try {
                db.closeConnection(false);
            } catch (Exception ex) {}
            return new LoadResult("ERROR: Internal server error", false);
        }
    }
}
