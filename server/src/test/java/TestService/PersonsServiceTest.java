package TestService;

import Service.PersonsService;
import Service.RegisterService;
import request.RegisterRequest;
import result.PersonsResult;
import data_access.DataAccessException;
import data_access.Database;
import data_access.PersonDAO;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PersonsServiceTest {
    private Database db = new Database();
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
    public void personsPass() throws DataAccessException {
        PersonsService ps = new PersonsService();
        PersonDAO pDAO = new PersonDAO(conn);
        ArrayList<Person> people = pDAO.getPersons(register.getUsername());
        PersonsResult test = ps.persons(register.getUsername());
        assertNotNull(test);
        assertTrue(test.isSuccess());
        assertNull(test.getMessage());
        for (Person p : people) {
            assertTrue(test.getData().contains(p));
        }
    }

    @Test
    public void personsFail() {
        PersonsService ps = new PersonsService();
        PersonsResult test = ps.persons("someOtherUsername");
        assertNotNull(test);
        assertTrue(test.isSuccess());
        assertNull(test.getMessage());
        assertEquals(0, test.getData().size());
    }
}