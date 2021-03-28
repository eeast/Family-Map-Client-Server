package data_access;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object for Users
 */
public class UserDAO {
    private final Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Method for adding a User Object to the database
     * @param u User Model containing applicable information
     */
    public void insertUser(User u) throws DataAccessException {
        String sql = "INSERT INTO users (username, password, email, firstname, lastname, gender, person_id) " +
                     "VALUES(?,?,?,?,?,?,?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getUsername());
            stmt.setString(2, u.getPassword());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, u.getFirst_name());
            stmt.setString(5, u.getLast_name());
            stmt.setString(6, u.getGender());
            stmt.setString(7, u.getPerson_id());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Method for retrieving a User Object from the database
     * @param username - String id of the User to be retrieved
     * @return - User object with matching parameters
     */
    public User getUser(String username)  throws DataAccessException{
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM users WHERE username = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("username"), rs.getString("password"),
                                rs.getString("email"), rs.getString("firstname"),
                                rs.getString("lastname"), rs.getString("gender"),
                                rs.getString("person_id"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Method for removing all User Objects from the database
     */
    public void clearUsers() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM users";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing users table");
        }
    }
}
