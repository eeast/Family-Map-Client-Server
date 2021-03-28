package Service;

import data_access.AuthTokenDAO;
import model.AuthToken;
import result.AuthorizationResult;

import java.sql.Connection;

/**
 * Authorization extension of Service class. Contains the function for looking up and verifying a given Authorization
 * Token with information found in the database.
 */
public class AuthorizationService extends Service {

    /**
     * Function for looking up and verifying a given Authorization Token is found in the database.
     * @param authtoken String Authorization Token (generally passed by the Client application) to be verified
     * @return AuthorizationResult Object, containing a boolean success indicator. Successful results will include the
     * AuthToken and Username; unsuccessful results will include a message indicating the error.
     */
    public AuthorizationResult authorize(String authtoken) {
        try {
            Connection conn = db.getConnection();
            if (authtoken!= null && !authtoken.equals("")) {
                atDAO = new AuthTokenDAO(conn);
                AuthToken token = atDAO.getAuthToken(authtoken);
                db.closeConnection(false);
                if (token != null) {
                    return new AuthorizationResult(token.getToken(), token.getUser_id());
                } else {
                    System.out.println("token: " + authtoken);
                    return new AuthorizationResult("ERROR: AuthToken not found");
                }
            } else {
                db.closeConnection(false);
                return new AuthorizationResult("ERROR: AuthToken not provided");
            }
        } catch (Exception e) {
            try {
                db.closeConnection(false);
            } catch (Exception ex) {}
        }
        return new AuthorizationResult("ERROR: Internal server error");
    }
}
