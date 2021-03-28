package request;

import model.Event;
import model.Person;
import model.User;

import java.util.List;

/**
 * Request Object for the receipt of LOAD requests from the client
 */
public class LoadRequest {
    /**
     * The List of Users to be added to the database
     */
    private final List<User> users;
    /**
     * The List of Persons to be added to the database
     */
    private final List<Person> persons;
    /**
     * The List of Events to be added to the database
     */
    private final List<Event> events;

    /**
     * Constructor for LoadRequest object with predefined parameters
     * @param users List of User objects to be added to the database
     * @param persons List of Person objects to be added to the database
     * @param events List of Event objects to be added to the database
     */
    public LoadRequest(List<User> users, List<Person> persons, List<Event> events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public List<Event> getEvents() {
        return events;
    }
}
