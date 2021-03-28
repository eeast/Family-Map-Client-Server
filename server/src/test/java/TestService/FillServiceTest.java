package TestService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import Service.FillService;
import Service.RegisterService;
import data_access.DataAccessException;
import data_access.Database;
import request.FillRequest;
import request.RegisterRequest;
import result.FillResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FillServiceTest {
    private final Database db = new Database();
    private Connection conn;
    private RegisterRequest register;

    @BeforeEach
    public void setup() throws DataAccessException {
        db.getConnection();
        db.clearTables();
        db.closeConnection(true);
        register = new RegisterRequest("user1", "password", "email", "Jane", "Goodall", "f");
        RegisterService rs = new RegisterService();
        rs.register(register);
        conn = db.getConnection();
    }

    @Test
    public void fillPassDefault() throws DataAccessException {
        FillService fs = new FillService();
        FillRequest request = new FillRequest(register.getUsername());
        FillResult result = fs.fill(request);
        assertTrue(result.isSuccess());
        assertEquals("Successfully added 31 persons and 91 events to the database.", result.getMessage());
    }

    @Test
    public void fillPassSixGen() throws DataAccessException {
        FillService fs = new FillService();
        FillRequest request = new FillRequest(register.getUsername(), 6);
        FillResult result = fs.fill(request);
        assertTrue(result.isSuccess());
        assertEquals("Successfully added 127 persons and 379 events to the database.", result.getMessage());
    }

    @Test
    public void fillFail() throws DataAccessException {
        FillService fs = new FillService();
        FillRequest request = new FillRequest("someOtherUsername");
        FillResult result = fs.fill(request);
        assertFalse(result.isSuccess());
        assertEquals("Invalid username or generations parameter", result.getMessage());
    }

}
