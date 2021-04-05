package main;

import java.util.HashSet;
import java.util.Set;

import model.AuthToken;
import model.Event;
import model.Person;

public class DataCache {
    private static DataCache instance;

    private AuthToken authToken;

    private Person user;

    private final Set<Person> allPersons = new HashSet<>();
    private final Set<Event> allEvents = new HashSet<>();

    private final Set<Person> immediateFamilyMales = new HashSet<>();
    private final Set<Person> immediateFamilyFemales = new HashSet<>();

    private final Set<Person> fatherSideMales = new HashSet<>();
    private final Set<Person> fatherSideFemales = new HashSet<>();
    private final Set<Person> motherSideMales = new HashSet<>();
    private final Set<Person> motherSideFemales = new HashSet<>();



    public static DataCache getInstance() {
        if(instance == null) {
            instance = new DataCache();
        }

        return instance;
    }

    private DataCache() {

    }

    public AuthToken getAuthToken() {
        return instance.authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        instance.authToken = authToken;
    }

    public Person getUser() {
        return instance.user;
    }

    public void setUser(Person user) {
        instance.user = user;
    }

    public Set<Person> getImmediateFamilyMales() {
        return instance.immediateFamilyMales;
    }

    public Set<Person> getImmediateFamilyFemales() {
        return instance.immediateFamilyFemales;
    }

    public Set<Person> getFatherSideMales() {
        return instance.fatherSideMales;
    }

    public Set<Person> getFatherSideFemales() {
        return instance.fatherSideFemales;
    }

    public Set<Person> getMotherSideMales() {
        return instance.motherSideMales;
    }

    public Set<Person> getMotherSideFemales() {
        return instance.motherSideFemales;
    }

    public Set<Event> getAllEvents() {
        return instance.allEvents;
    }

    public Set<Person> getAllPersons() {
        return instance.allPersons;
    }

    public void loadFamily() {

    }
}
