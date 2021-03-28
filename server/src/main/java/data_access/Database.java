package data_access;

import java.sql.*;

public class Database {
    private Connection conn;

    public Connection openConnection() throws DataAccessException {
        try {
            final String CONNECTION_URL = "jdbc:sqlite:familyMap.sqlite";
            conn = DriverManager.getConnection(CONNECTION_URL);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }

        return conn;
    }

    public Connection getConnection() throws DataAccessException {
        if(conn == null) {
            return openConnection();
        } else {
            return conn;
        }
    }

    public void closeConnection(boolean commit) throws DataAccessException {
        try {
            if (commit) {
                conn.commit();
            } else {
                conn.rollback();
            }

            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    public void clearTables() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM auth_tokens";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM events";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM users";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM persons";
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing events table");
        }
    }

    public void clearData(String username) throws DataAccessException {
        String sql = "DELETE FROM events WHERE assoc_username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //delete events
            stmt.setString(1, username);
            stmt.execute();
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing events and persons tables");
        }

        sql = "DELETE FROM persons WHERE assoc_username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //delete persons
            stmt.setString(1, username);
            stmt.execute();
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing events and persons tables");
        }

    }
}
