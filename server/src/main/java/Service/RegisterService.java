package Service;

import data_access.AuthTokenDAO;
import data_access.DataAccessException;
import data_access.UserDAO;
import model.AuthToken;
import model.User;
import request.FillRequest;
import request.RegisterRequest;
import result.RegisterResult;

import java.sql.Connection;
import java.util.UUID;

/**
 * Register extension of the Service class. Contains the function for validating a request for registration and for
 * adding the user to the database. Also produces an AuthToken for the new user and logs them in.
 */
public class RegisterService extends Service {

    /**
     * Creates a new user account, generates 4 generations of ancestor data for the new user, logs the user in,
     * and returns an auth token.
     * @param r is the RegisterRequest object with the required parameters for creating a User
     * @return RegisterResult object with a boolean success identifier, and if successful, the authorization token,
     * username, and person_id. If unsuccessful, a String error message is included identifying the issue.
     */
    public RegisterResult register(RegisterRequest r) {
        User u = new User(r.getUsername(), r.getPassword(), r.getEmail(), r.getFirst_name(), r.getLast_name(),
                r.getGender(), UUID.randomUUID().toString());
        AuthToken authtoken;
        Connection conn;
        try {
            conn = db.getConnection();
            if (u.isValid()) {
                uDAO = new UserDAO(conn);
                User test = uDAO.getUser(u.getUsername());
                if (test == null) {
                    uDAO.insertUser(u);
                    authtoken = new AuthToken(UUID.randomUUID().toString(), u.getUsername());
                    atDAO = new AuthTokenDAO(conn);
                    atDAO.insertAuthToken(authtoken);
                    db.closeConnection(true);
                    FillRequest fillRequest = new FillRequest(u.getUsername());
                    FillService fs = new FillService();
                    fs.fill(fillRequest);
                } else {
                    db.closeConnection(false);
                    return new RegisterResult("ERROR: Username already taken by another user");
                }
            } else {
                db.closeConnection(false);
                return new RegisterResult("ERROR: Request property missing or has invalid value");
            }
        } catch (Exception e) {
            try {
                db.closeConnection(false);
            } catch (DataAccessException dataAccessException) {}
            return new RegisterResult("ERROR: Internal server error");
        }
        return new RegisterResult(authtoken.getToken(), u.getUsername(), u.getPerson_id());
    }
}
