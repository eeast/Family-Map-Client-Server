package TestService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Service.ClearService;
import Service.RegisterService;
import data_access.DataAccessException;
import data_access.Database;
import request.RegisterRequest;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {
    private final Database db = new Database();
    private RegisterRequest register;

    @BeforeEach
    void setUp() throws DataAccessException {
        db.getConnection();
        db.clearTables();
        db.closeConnection(true);
        register = new RegisterRequest("user1", "password", "email", "Jane", "Goodall", "f");

    }

    @Test
    public void clearServicePass() {
        RegisterService rs = new RegisterService();
        rs.register(register);
        assertFalse(rs.register(register).isSuccess());
        ClearService cs = new ClearService();
        cs.clearTables();
        assertTrue(rs.register(register).isSuccess());
    }
}