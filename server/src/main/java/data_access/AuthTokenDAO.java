package data_access;

import model.AuthToken;

import java.sql.*;
import java.util.*;

/**
 * Data Access Object for Authorization Tokens
 */
public class AuthTokenDAO {
    private final Connection conn;

    public AuthTokenDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Method for adding an AuthToken to the database
     * @param a A randomly generated Authorization Token generated upon successful login by a User
     */
    public void insertAuthToken(AuthToken a) throws DataAccessException {
        String sql = "INSERT INTO auth_tokens (auth_token, user_id) VALUES(?,?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, a.getToken());
            stmt.setString(2, a.getUser_id());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Method for retrieving an AuthToken from the database
     * @param id - String id of the AuthToken to be retrieved
     * @return - AuthToken object with matching parameters
     */
    public AuthToken getAuthToken(String id) throws DataAccessException {
        AuthToken authToken;
        ResultSet rs;
        String sql = "SELECT * FROM auth_tokens WHERE auth_token=?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authToken = new AuthToken(rs.getString("auth_token"), rs.getString("user_id"));
                return authToken;
            }
        } catch (Exception e) {
            throw new DataAccessException("Error encountered while retrieving Auth Token");
        }
        return null;
    }

    /**
     * Method for removing all AuthToken Objects from the database
     */
    public void clearAuthTokens() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM auth_tokens";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing auth_tokens table");
        }
    }
}
