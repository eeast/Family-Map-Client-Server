package TestService;

import Service.EventsService;
import Service.RegisterService;
import request.RegisterRequest;
import result.EventsResult;
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

class EventsServiceTest {
    private final Database db = new Database();
    private Connection conn;
    private RegisterRequest register;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db.getConnection();
        db.clearTables();
        db.closeConnection(true);
        register = new RegisterRequest("user1", "password", "email", "Jane", "Goodall", "f");
        RegisterService rs = new RegisterService();
        rs.register(register);
        conn = db.getConnection();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void eventsPass() throws DataAccessException {
        EventsService es = new EventsService();
        EventDAO eDAO = new EventDAO(conn);
        ArrayList<Event> events = eDAO.getEvents(register.getUsername());
        EventsResult test = es.events(register.getUsername());
        assertNotNull(test);
        assertTrue(test.isSuccess());
        for (Event e : events) {
            assertTrue(test.getData().contains(e));
        }
    }

    @Test
    public void eventsFail() throws DataAccessException{
        EventsService es = new EventsService();
        EventsResult test = es.events("someOtherUsername");
        assertNotNull(test);
        assertFalse(test.isSuccess());
        assertNull(test.getData());
        assertNotNull(test.getMessage());
    }

}