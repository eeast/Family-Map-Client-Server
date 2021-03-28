package TestService;

import Service.LoginService;
import Service.RegisterService;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;
import data_access.DataAccessException;
import data_access.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {
    private Database db = new Database();
    private RegisterRequest regRequest;
    private RegisterResult regResult;
    private RegisterService rs = new RegisterService();


    @BeforeEach
    void setUp() throws DataAccessException {
        db.getConnection();
        db.clearTables();
        db.closeConnection(true);
        regRequest = new RegisterRequest("user1", "password", "email", "Jane", "Goodall", "f");
        regResult = rs.register(regRequest);
    }

    @Test
    public void loginPass() {
        LoginService ls = new LoginService();
        LoginRequest request = new LoginRequest(regRequest.getUsername(), regRequest.getPassword());
        LoginResult result = ls.login(request);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getPerson_id());
        assertNotNull(result.getUsername());
        assertNotNull(result.getAuthtoken());
        assertNull(result.getMessage());
    }

    @Test
    public void loginBadPassword() {
        LoginService ls = new LoginService();
        LoginRequest request = new LoginRequest(regRequest.getUsername(), "someOtherPassword");
        LoginResult result = ls.login(request);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertNull(result.getPerson_id());
        assertNull(result.getUsername());
        assertNull(result.getAuthtoken());
        assertNotNull(result.getMessage());
    }

    @Test
    public void loginBadUsername() {
        LoginService ls = new LoginService();
        LoginRequest request = new LoginRequest("someOtherUsername", regRequest.getPassword());
        LoginResult result = ls.login(request);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertNull(result.getPerson_id());
        assertNull(result.getUsername());
        assertNull(result.getAuthtoken());
        assertNotNull(result.getMessage());
    }
}