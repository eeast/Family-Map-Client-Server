package TestDAO;

import data_access.*;
import model.Person;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest {
    private Database db;
    private Person bestPerson;
    private Person secondPerson;
    private Person badClone;
    private PersonDAO pDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        bestPerson = new Person("JohnGuy123A","JohnGuy","John","Guy","m",
                null,null,"JaneGoodall123A");
        secondPerson = new Person("JaneGoodall123A","JohnGuy","Jane","Goodall",
                "f",null,null,"JohnGuy123A");
        badClone = new Person("JohnGuy123B","JohnGuy","John","Gui","m");
        Connection conn = db.getConnection();
        db.clearTables();
        pDao = new PersonDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        pDao.insertPerson(bestPerson);
        Person compareTest = pDao.getPerson(bestPerson.getPerson_id());
        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        pDao.insertPerson(bestPerson);
        assertThrows(DataAccessException.class, ()-> pDao.insertPerson(bestPerson));
    }

    @Test
    public void findPass() throws DataAccessException {
        pDao.insertPerson(bestPerson);
        Person compareTest = pDao.getPerson(bestPerson.getPerson_id());
        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest);
        assertNotEquals(secondPerson, compareTest);
        assertNotEquals(badClone, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        pDao.insertPerson(bestPerson);
        Person compareTest = pDao.getPerson(bestPerson.getPerson_id());
        assertNotNull(compareTest);
        assertNotEquals(secondPerson, compareTest);
        assertNotEquals(badClone, compareTest);
        assertNull(pDao.getPerson(secondPerson.getPerson_id()));
        assertNull(pDao.getPerson(badClone.getPerson_id()));
    }

    @Test
    public void clearTest() throws DataAccessException {
        pDao.insertPerson(bestPerson);
        assertNotNull(pDao.getPerson(bestPerson.getPerson_id()));
        pDao.clearPersons();
        assertNull(pDao.getPerson(bestPerson.getPerson_id()));
    }

    @Test
    public void listPass() throws DataAccessException {
        ArrayList<Person> people = new ArrayList<Person>(Arrays.asList(bestPerson, secondPerson, badClone));
        pDao.insertPerson(bestPerson);
        pDao.insertPerson(secondPerson);
        pDao.insertPerson(badClone);
        ArrayList<Person> copy = pDao.getPersons(bestPerson.getAssoc_username());
        assertNotNull(copy);
        for (Person p : copy) {
            assertNotNull(p);
        }
        for (Person p : people) {
            assertTrue(copy.contains(p));
        }
    }

    @Test
    public void listFail() throws DataAccessException {
        ArrayList<Person> people = new ArrayList<Person>(Arrays.asList(bestPerson, secondPerson, badClone));
        pDao.insertPerson(bestPerson);
        pDao.insertPerson(secondPerson);
        pDao.insertPerson(badClone);
        ArrayList<Person> copy = pDao.getPersons("somethingelse");
        assertTrue(copy.size() == 0);
        for (Person p : people) {
            assertFalse(copy.contains(p));
        }
    }
}
