package TestService;

import Service.PersonService;
import Service.RegisterService;
import request.RegisterRequest;
import result.PersonResult;
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

class PersonServiceTest {
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
    public void personPass() throws DataAccessException {
        PersonService ps = new PersonService();
        PersonDAO pDAO = new PersonDAO(conn);
        ArrayList<Person> people = pDAO.getPersons(register.getUsername());
        for (Person p : people) {
            PersonResult test = ps.person(p.getPerson_id(), register.getUsername());
            assertNotNull(test);
            assertTrue(test.isSuccess());
            assertNull(test.getMessage());
            assertEquals(register.getUsername(), test.getAssoc_username());
        }
    }

    @Test
    public void personFail() throws DataAccessException {
        PersonService ps = new PersonService();
        PersonDAO pDAO = new PersonDAO(conn);
        ArrayList<Person> people = pDAO.getPersons(register.getUsername());
        for (Person p : people) {
            PersonResult test = ps.person(p.getPerson_id(), "someOtherUsername");
            assertNotNull(test);
            assertFalse(test.isSuccess());
            assertNotNull(test.getMessage());
            assertNotEquals(register.getUsername(), test.getAssoc_username());
        }
        PersonResult test = ps.person("someOtherPersonID", register.getUsername());
        assertNotNull(test);
        assertFalse(test.isSuccess());
        assertNotNull(test.getMessage());
        assertNotEquals(register.getUsername(), test.getAssoc_username());
    }
}