package Service;

import data_access.AuthTokenDAO;
import data_access.UserDAO;
import model.AuthToken;
import model.User;
import request.LoginRequest;
import result.LoginResult;

import java.sql.Connection;
import java.util.UUID;

/**
 * Login extension of the Service class. Contains the function for verifying correct username and password, as well as
 * generating a new AuthToken upon successful confirmation.
 */
public class LoginService extends Service {

    /**
     * Logs in the user and returns an auth token.
     * @param r the LoginRequest object with the required parameters for login (username and password)
     * @return LoginResult object with a boolean success identifier, and if successful, the authorization token,
     * username, and person_id. If unsuccessful, a String error message is included identifying the issue.
     */
    public LoginResult login(LoginRequest r) {
        Connection conn;
        try {
            conn = db.getConnection();
            uDAO = new UserDAO(conn);
            User u = uDAO.getUser(r.getUsername());
            if (u != null && u.getPassword().equals(r.getPassword())) {
                AuthToken authtoken = new AuthToken(UUID.randomUUID().toString(), u.getUsername());
                atDAO = new AuthTokenDAO(conn);
                atDAO.insertAuthToken(authtoken);
                db.closeConnection(true);
                return new LoginResult(authtoken.getToken(), u.getUsername(), u.getPerson_id());
            } else {
                db.closeConnection(false);
                return new LoginResult("ERROR: Request property missing or has invalid value");
            }

        } catch (Exception e) {
            try {
                db.closeConnection(false);
            } catch (Exception ex) {}
            return new LoginResult("ERROR: Internal server error");
        }

    }
}
