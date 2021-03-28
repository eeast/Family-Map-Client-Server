package data_access;

import model.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Events
 */
public class EventDAO {
    private final Connection conn;

    public EventDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Method for adding an Event Object to the database
     * @param e Event Model containing applicable parameters
     */
    public void insertEvent(Event e) throws DataAccessException {
        String sql = "INSERT INTO events (event_id, assoc_username, person_id, latitude, longitude, " +
                "country, city, event_type, year) VALUES(?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, e.getEvent_id());
            stmt.setString(2, e.getAssoc_username());
            stmt.setString(3, e.getPerson_id());
            stmt.setFloat(4, e.getLatitude());
            stmt.setFloat(5, e.getLongitude());
            stmt.setString(6, e.getCountry());
            stmt.setString(7, e.getCity());
            stmt.setString(8, e.getEvent_type());
            stmt.setInt(9, e.getYear());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Method for retrieving an Event Object from the database
     * @param id - String id of the Event to be retrieved
     * @return - Event object with matching parameters
     */
    public Event getEvent(String id) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM events WHERE event_id = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("event_id"), rs.getString("assoc_username"),
                        rs.getString("person_id"), rs.getFloat("latitude"),
                        rs.getFloat("longitude"), rs.getString("country"),
                        rs.getString("city"), rs.getString("event_type"),
                        rs.getInt("year"));

                return event;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public Event getEventType(String person_id, String type) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM events WHERE person_id = ? AND event_type = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person_id);
            stmt.setString(2, type);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("event_id"), rs.getString("assoc_username"),
                        rs.getString("person_id"), rs.getFloat("latitude"),
                        rs.getFloat("longitude"), rs.getString("country"),
                        rs.getString("city"), rs.getString("event_type"),
                        rs.getInt("year"));

                return event;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Method for retrieving all Event Objects for a Person from the database
     * @param username - String username of user for which all events are to be pulled
     * @return - List of Events requested
     */
    public ArrayList<Event> getEvents(String username) throws DataAccessException {
        Event event;
        ArrayList<Event> events = new ArrayList<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM events WHERE assoc_username = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                event = new Event(rs.getString("event_id"), rs.getString("assoc_username"),
                        rs.getString("person_id"), rs.getFloat("latitude"),
                        rs.getFloat("longitude"), rs.getString("country"),
                        rs.getString("city"), rs.getString("event_type"),
                        rs.getInt("year"));
                events.add(event);
            }
            return events;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding events");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Method for removing all Event Objects from the database
     */
    public void clearEvents() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM events";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing persons table");
        }
    }
}
