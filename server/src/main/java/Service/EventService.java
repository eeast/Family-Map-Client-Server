package Service;

import data_access.EventDAO;
import model.Event;
import result.EventResult;

import java.sql.Connection;

/**
 * Event lookup extension of the Service class. Contains the function for looking up a single Event object. A username
 * is also required, for confirmation of authorized access.
 */
public class EventService extends Service{

    /**
     * Returns the single Event object with the specified ID.
     * @param id String id of the Event requested
     * @param username String username of the requesting user used for confirmation of authorized access
     * @return EventResult object with a boolean success identifier, and if successful, the data from the Event
     * object requested. If unsuccessful, a String error message is included identifying the issue.
     */
    public EventResult event(String id, String username) {
        try {
            Connection conn = db.getConnection();
            eDAO = new EventDAO(conn);
            Event e = eDAO.getEvent(id);
            db.closeConnection(false);
            if (e != null) {
                if (e.getAssoc_username().equals(username)) {
                    return new EventResult(e.getAssoc_username(), e.getEvent_id(), e.getPerson_id(), e.getLatitude(),
                            e.getLongitude(), e.getCountry(), e.getCity(), e.getEvent_type(), e.getYear());
                } else {
                    return new EventResult("ERROR: Requested event does not belong to this user");
                }
            } else {
                return new EventResult("ERROR: Invalid eventID parameter");
            }
        } catch (Exception e) {
            try {
                db.closeConnection(false);
            } catch (Exception ex) {}
        }
        return new EventResult("ERROR: Internal server error");
    }
}
