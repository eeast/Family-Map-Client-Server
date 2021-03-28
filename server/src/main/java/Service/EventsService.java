package Service;

import data_access.EventDAO;
import model.Event;
import result.EventsResult;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Events lookup extension of the Service class. Contains the function for looking up ALL Event objects associated with
 * a given username.
 */
public class EventsService extends Service {

    /**
     * Returns ALL events for ALL family members of the current user. The current user is determined from the
     * provided auth token using the AuthorizationService.
     * @param username String username of the user requesting Events data; used in query
     * @return EventsResult object with a boolean success identifier, and if successful, a list of ALL Event objects
     * associated with ALL Person objects associated with the current user. If unsuccessful, a String error message
     * is included identifying the issue.
     */
    public EventsResult events(String username) {
        try {
            Connection conn = db.getConnection();
            eDAO = new EventDAO(conn);
            ArrayList<Event> events = eDAO.getEvents(username);
            db.closeConnection(false);
            if (events.size() > 0) {
                return new EventsResult(events);
            } else {
                return new EventsResult("ERROR: Unable to find any Events associated with the current user");
            }
        } catch (Exception e) {
            try {
                db.closeConnection(false);
            } catch (Exception ex) {}
        }
        return new EventsResult("ERROR: Internal server error");
    }
}
