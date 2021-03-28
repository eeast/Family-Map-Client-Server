package TestService;

import Service.RegisterService;
import request.RegisterRequest;
import result.RegisterResult;
import data_access.DataAccessException;
import data_access.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {
    private Database db = new Database();
    private RegisterRequest goodRequest;
    private RegisterRequest badRequest;

    @BeforeEach
    void setUp() throws DataAccessException {
        db.getConnection();
        db.clearTables();
        db.closeConnection(true);
        goodRequest = new RegisterRequest("user1", "password", "email", "Jane", "Goodall", "f");
        badRequest = new RegisterRequest("user2", "password", "email", "John", "Doe", "m/f");
    }

    @Test
    public void registerPass() {
        RegisterService rs = new RegisterService();
        RegisterResult result = rs.register(goodRequest);
        assertTrue(result.isSuccess());
        assertNotNull(result);
        assertEquals(goodRequest.getUsername(), result.getUsername());
        assertNotNull(result.getAuth_token());
        assertNotNull(result.getPerson_id());
        assertNull(result.getMessage());
    }

    @Test
    public void registerInvalidRequest() {
        RegisterService rs = new RegisterService();
        RegisterResult badResult = rs.register(badRequest);
        assertFalse(badResult.isSuccess());
        assertNotNull(badResult);
        assertNull(badResult.getUsername());
        assertNull(badResult.getAuth_token());
        assertNull(badResult.getPerson_id());
        assertNotNull(badResult.getMessage());
        rs.register(goodRequest);
        RegisterResult goodResult = rs.register(goodRequest);
        assertFalse(goodResult.isSuccess());
        assertNotNull(goodResult);
        assertNull(goodResult.getUsername());
        assertNull(goodResult.getAuth_token());
        assertNull(goodResult.getPerson_id());
        assertNotNull(goodResult.getMessage());
    }
}