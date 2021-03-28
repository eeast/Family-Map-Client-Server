package TestService;

import Service.AuthorizationService;
import Service.RegisterService;
import request.RegisterRequest;
import data_access.*;
import org.junit.jupiter.api.*;
import result.*;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorizationServiceTest {
    private final Database db = new Database();
    private RegisterRequest regRequest;
    private RegisterResult regResult;
    private final RegisterService rs = new RegisterService();


    @BeforeEach
    public void setup() throws DataAccessException {
        db.getConnection();
        db.clearTables();
        db.closeConnection(true);
        regRequest = new RegisterRequest("user1", "password", "email", "Jane", "Goodall", "f");
        regResult = rs.register(regRequest);
    }

    @Test
    public void authorizePass() throws DataAccessException {
        AuthorizationService as = new AuthorizationService();
        AuthorizationResult ar = as.authorize(regResult.getAuth_token());
        assertNotNull(ar);
        assertTrue(ar.isSuccess());
        assertEquals(regRequest.getUsername(), ar.getUsername());
    }

    @Test
    public void authorizeFail() {
        AuthorizationService as = new AuthorizationService();
        AuthorizationResult ar = as.authorize("someFakeToken");
        assertNotNull(ar);
        assertFalse(ar.isSuccess());
        assertNotEquals(regRequest.getUsername(), ar.getUsername());
    }
}
