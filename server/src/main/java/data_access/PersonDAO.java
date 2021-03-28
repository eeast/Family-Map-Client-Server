package data_access;

import model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * Data Access Object for Persons
 */
public class PersonDAO {
    private final Connection conn;

    public PersonDAO(Connection conn) {
        this.conn = conn;
    }
    /**
     * Method for adding a Person Object to the database
     * @param p Person Model containing applicable parameters
     */
    public void insertPerson(Person p) throws DataAccessException {
        String sql = "INSERT INTO persons (person_id, assoc_username, first_name, last_name, gender, " +
                     "father_id, mother_id, spouse_id) VALUES(?,?,?,?,?,?,?,?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getPerson_id());
            stmt.setString(2, p.getAssoc_username());
            stmt.setString(3, p.getFirst_name());
            stmt.setString(4, p.getLast_name());
            stmt.setString(5, p.getGender());
            stmt.setString(6, p.getFather_id());
            stmt.setString(7, p.getMother_id());
            stmt.setString(8, p.getSpouse_id());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Method for retrieving a Person Object from the database
     * @param id String id of the Person to be retrieved
     * @return Person object with matching parameters
     */
    public Person getPerson(String id) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM persons WHERE person_id = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("person_id"), rs.getString("assoc_username"),
                                    rs.getString("first_name"), rs.getString("last_name"),
                                    rs.getString("gender"), rs.getString("father_id"),
                                    rs.getString("mother_id"), rs.getString("spouse_id"));
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
        } finally {
            if (rs != null) {
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
     * Method for retrieving ALL Person Objects associated with a given user from the database
     * @return - List of Persons objects
     */
    public ArrayList<Person> getPersons(String username) throws DataAccessException {
        Person person;
        ArrayList<Person> people = new ArrayList<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM persons WHERE assoc_username = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                person = new Person(rs.getString("person_id"), rs.getString("assoc_username"),
                        rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("gender"), rs.getString("father_id"),
                        rs.getString("mother_id"), rs.getString("spouse_id"));
                people.add(person);
            }
            return people;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Method for removing all Person Objects from the database
     */
    public void clearPersons() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM persons";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing persons table");
        }
    }
}
