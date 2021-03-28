package TestDAO;

import data_access.*;
import model.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private Database db;
    private User bestUser;
    private User secondUser;
    private User badClone;
    private UserDAO uDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        bestUser = new User("JohnGuy", "password1", "johnguy@something.com", "John",
                            "Guy", "m", "JohnGuy123A");
        secondUser = new User("JaneGoodall", "password2", "janegoodall@something.com",
                             "Jane", "Goodall", "f", "JaneGoodall123A");
        badClone = new User("JohnGui", "password1", "johnguy@something.com", "John",
                            "Guy", "m", "JohnGuy123A");
        Connection conn = db.getConnection();
        db.clearTables();
        uDao = new UserDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        uDao.insertUser(bestUser);
        User compareTest = uDao.getUser(bestUser.getUsername());
        assertNotNull(compareTest);
        assertEquals(bestUser, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        uDao.insertUser(bestUser);
        assertThrows(DataAccessException.class, ()-> uDao.insertUser(bestUser));
    }

    @Test
    public void findPass() throws DataAccessException {
        uDao.insertUser(bestUser);
        User compareTest = uDao.getUser(bestUser.getUsername());
        assertNotNull(compareTest);
        assertEquals(bestUser, compareTest);
        assertNotEquals(secondUser, compareTest);
        assertNotEquals(badClone, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        uDao.insertUser(bestUser);
        User compareTest = uDao.getUser(bestUser.getUsername());
        assertNotNull(compareTest);
        assertNotEquals(secondUser, compareTest);
        assertNotEquals(badClone, compareTest);
        assertNull(uDao.getUser(secondUser.getUsername()));
        assertNull(uDao.getUser(badClone.getUsername()));
    }

    @Test
    public void clearTest() throws DataAccessException {
        uDao.insertUser(bestUser);
        assertNotNull(uDao.getUser(bestUser.getUsername()));
        uDao.clearUsers();
        assertNull(uDao.getUser(bestUser.getUsername()));
    }
}
