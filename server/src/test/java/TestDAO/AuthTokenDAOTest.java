package TestDAO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import data_access.AuthTokenDAO;
import data_access.DataAccessException;
import data_access.Database;
import model.AuthToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AuthTokenDAOTest {
    private Database db;
    private AuthToken bestAuthToken;
    private AuthToken secondAuthToken;
    private AuthToken badClone;
    private AuthTokenDAO atDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        bestAuthToken = new AuthToken("firstToken123","JohnGuy");
        secondAuthToken = new AuthToken("nextToken123","JaneGoodall");
        badClone = new AuthToken("lastToken123","JohnGui");
        Connection conn = db.getConnection();
        db.clearTables();
        atDao = new AuthTokenDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        atDao.insertAuthToken(bestAuthToken);
        AuthToken compareTest = atDao.getAuthToken(bestAuthToken.getToken());
        assertNotNull(compareTest);
        assertEquals(bestAuthToken, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        atDao.insertAuthToken(bestAuthToken);
        assertThrows(DataAccessException.class, ()-> atDao.insertAuthToken(bestAuthToken));
    }

    @Test
    public void findPass() throws DataAccessException {
        atDao.insertAuthToken(bestAuthToken);
        AuthToken compareTest = atDao.getAuthToken(bestAuthToken.getToken());
        assertNotNull(compareTest);
        assertEquals(bestAuthToken, compareTest);
        assertNotEquals(secondAuthToken, compareTest);
        assertNotEquals(badClone, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        atDao.insertAuthToken(bestAuthToken);
        AuthToken compareTest = atDao.getAuthToken(bestAuthToken.getToken());
        assertNotNull(compareTest);
        assertNotEquals(secondAuthToken, compareTest);
        assertNotEquals(badClone, compareTest);
        assertNull(atDao.getAuthToken(secondAuthToken.getUser_id()));
        assertNull(atDao.getAuthToken(badClone.getUser_id()));
    }

    @Test
    public void clearTest() throws DataAccessException {
        atDao.insertAuthToken(bestAuthToken);
        assertNotNull(atDao.getAuthToken(bestAuthToken.getToken()));
        atDao.clearAuthTokens();
        assertNull(atDao.getAuthToken(bestAuthToken.getUser_id()));
    }
}
