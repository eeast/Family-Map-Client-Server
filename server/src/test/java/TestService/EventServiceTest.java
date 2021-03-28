package TestService;

import Service.EventService;
import Service.RegisterService;
import request.RegisterRequest;
import result.EventResult;
import data_access.DataAccessException;
import data_access.Database;
import data_access.EventDAO;
import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EventServiceTest {
    private final Database db = new Database();
    private Connection conn;
    private RegisterRequest register;

    @BeforeEach
    void setUp() throws DataAccessException {
        db.getConnection();
        db.clearTables();
        db.closeConnection(true);
        register = new RegisterRequest("user1", "password", "email", "Jane", "Goodall", "f");
        RegisterService rs = new RegisterService();
        rs.register(register);
        conn = db.getConnection();
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void eventPass() throws DataAccessException{
        EventService es = new EventService();
        EventDAO eDAO = new EventDAO(conn);
        ArrayList<Event> events = eDAO.getEvents(register.getUsername());
        for (Event e : events) {
            EventResult test = es.event(e.getEvent_id(), register.getUsername());
            assertNotNull(test);
            assertTrue(test.isSuccess());
            assertEquals(register.getUsername(), test.getAssoc_username());
            assertNull(test.getMessage());
        }
    }

    @Test
    public void eventFail() throws DataAccessException {
        EventService es = new EventService();
        EventDAO eDAO = new EventDAO(conn);
        ArrayList<Event> events = eDAO.getEvents(register.getUsername());
        for (Event e : events) {
            EventResult test = es.event(e.getEvent_id(), "someOtherUsername");
            assertNotNull(test);
            assertFalse(test.isSuccess());
            assertNotNull(test.getMessage());
            assertNotEquals(register.getUsername(), test.getAssoc_username());
        }
        EventResult test = es.event("someOtherEventID", register.getUsername());
        assertNotNull(test);
        assertFalse(test.isSuccess());
        assertNotNull(test.getMessage());
        assertNotEquals(register.getUsername(), test.getAssoc_username());
    }
}