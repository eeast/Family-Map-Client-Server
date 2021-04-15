package dataManagement;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import model.AuthToken;
import model.Event;
import model.Person;

public class DataCacheTest extends TestCase {
    private DataCache dataCache;
    private List<Person> people;
    private List<Event> allEvents;
    private Person user;

    private static final int LIFE_LINES_TOGGLE = 0;
    private static final int FAMILY_LINES_TOGGLE = 1;
    private static final int SPOUSE_LINES_TOGGLE = 2;
    private static final int FATHER_SIDE_TOGGLE = 3;
    private static final int MOTHER_SIDE_TOGGLE = 4;
    private static final int MALE_EVENTS_TOGGLE = 5;
    private static final int FEMALE_EVENTS_TOGGLE = 6;

    public void setUp() throws Exception {
        dataCache = DataCache.getInstance();
        user = new Person("user", "username", "Nancy", "Jobs", "f", "papa", "mama", "hubby");
        dataCache.setUser(user);
        people = new ArrayList<>();
        people.add(new Person("mama", "username", "Jane", "Goodall", "f"));
        people.add(new Person("papa", "username", "John", "Walker", "m"));
        people.add(new Person("hubby", "username", "Steve", "Jobs", "m", null, null, "user"));
        people.add(user);
        dataCache.getAllPersons().addAll(people);
        dataCache.sortFamily();
        allEvents = new ArrayList<>();
        allEvents.add(new Event("id1", "username", "user", (float)10.0, (float)30.0, "Tanzania", "Local", "Swimming", 2042));
        allEvents.add(new Event("id2", "username", "papa", (float)20.0, (float)40.0, "Tanzania", "Local", "Swimming", 2072));
        allEvents.add(new Event("id3", "username", "hubby", (float)30.0, (float)50.0, "Tanzania", "Coastal", "Swimming", 2012));
        dataCache.getAllEvents().addAll(allEvents);
    }

    public void tearDown() throws Exception {
        dataCache.clear();
    }

    public void testGetInstance() {
        DataCache dataCache1 = DataCache.getInstance();
        DataCache dataCache2 = DataCache.getInstance();
        assertEquals(dataCache1, dataCache2);
        assertEquals(dataCache, dataCache1);
        assertEquals(dataCache, dataCache2);
    }

    public void testGetAuthToken() {
        AuthToken authToken = new AuthToken("token", "username");
        dataCache.setAuthToken(authToken);
        AuthToken testToken = dataCache.getAuthToken();
        assertEquals(authToken, testToken);
    }

    public void testSetAuthToken() {
        AuthToken authToken = new AuthToken("token", "username");
        dataCache.setAuthToken(authToken);
        assertNotNull(dataCache.getAuthToken());
        assertEquals(authToken, dataCache.getAuthToken());
    }

    public void testSetUser() {
        assertEquals(user, dataCache.getUser());
        Person bestUser = new Person("personID", "username", "Jane", "Goodall", "f", "papa", "mama", "hubby");
        dataCache.setUser(bestUser);
        assertNotNull(dataCache.getUser());
        assertEquals(bestUser, dataCache.getUser());
    }

    public void testGetUser() {
        Person bestUser = new Person("personID", "username", "Jane", "Goodall", "f", "papa", "mama", "hubby");
        dataCache.setUser(bestUser);
        Person testUser = dataCache.getUser();
        assertEquals(bestUser, testUser);
    }

    public void testGetFilteredEvents() {
        dataCache.setOptionToggle(MALE_EVENTS_TOGGLE, false);
        Set<Event> testEvents = dataCache.getFilteredEvents();
        assertTrue(testEvents.contains(allEvents.get(0)));
        assertFalse(testEvents.contains(allEvents.get(1)));
        assertFalse(testEvents.contains(allEvents.get(2)));

        dataCache.setOptionToggle(MALE_EVENTS_TOGGLE, true);
        dataCache.setOptionToggle(FATHER_SIDE_TOGGLE, false);
        testEvents = dataCache.getFilteredEvents();
        assertTrue(testEvents.contains(allEvents.get(0)));
        assertFalse(testEvents.contains(allEvents.get(1)));
        assertTrue(testEvents.contains(allEvents.get(2)));

        dataCache.setOptionToggle(FEMALE_EVENTS_TOGGLE, false);
        testEvents = dataCache.getFilteredEvents();
        assertFalse(testEvents.contains(allEvents.get(0)));
        assertFalse(testEvents.contains(allEvents.get(1)));
        assertTrue(testEvents.contains(allEvents.get(2)));

        dataCache.setOptionToggle(FATHER_SIDE_TOGGLE, true);
        testEvents = dataCache.getFilteredEvents();
        assertFalse(testEvents.contains(allEvents.get(0)));
        assertTrue(testEvents.contains(allEvents.get(1)));
        assertTrue(testEvents.contains(allEvents.get(2)));
    }

    public void testGetAllEvents() {
        Set<Event> testEvents = dataCache.getAllEvents();
        for (Event e : allEvents) {
            assertTrue(testEvents.contains(e));
        }
    }

    public void testGetAllPersons() {
        Set<Person> testPeople = dataCache.getAllPersons();
        for (Person p : people) {
            assertTrue(testPeople.contains(p));
        }
    }

    public void testFindPerson() {
        assertEquals(user, dataCache.findPerson(allEvents.get(0).getPerson_id())); //user event
        assertEquals(people.get(1), dataCache.findPerson(allEvents.get(1).getPerson_id())); //papa event
        assertEquals(people.get(2), dataCache.findPerson(allEvents.get(2).getPerson_id())); //hubby event
    }

    public void testFindEvent() {
        for(Event e : allEvents) {
            assertEquals(e, dataCache.findEvent(e.getEvent_id()));
        }
    }

    public void testGetPersonalEvents() {
        //user personal events
        String personID = user.getPerson_id();
        List<Event> personalEvents = dataCache.getPersonalEvents(personID);
        assertEquals(1, personalEvents.size());
        assertTrue(personalEvents.contains(allEvents.get(0)));
        assertFalse(personalEvents.contains(allEvents.get(1)));
        assertFalse(personalEvents.contains(allEvents.get(2)));

        //mama personal events
        personID = people.get(0).getPerson_id();
        personalEvents = dataCache.getPersonalEvents(personID);
        assertEquals(0, personalEvents.size());
        assertFalse(personalEvents.contains(allEvents.get(0)));
        assertFalse(personalEvents.contains(allEvents.get(1)));
        assertFalse(personalEvents.contains(allEvents.get(2)));

        //papa personal events
        personID = people.get(1).getPerson_id();
        personalEvents = dataCache.getPersonalEvents(personID);
        assertEquals(1, personalEvents.size());
        assertFalse(personalEvents.contains(allEvents.get(0)));
        assertTrue(personalEvents.contains(allEvents.get(1)));
        assertFalse(personalEvents.contains(allEvents.get(2)));

        //hubby personal events
        personID = people.get(2).getPerson_id();
        personalEvents = dataCache.getPersonalEvents(personID);
        assertEquals(1, personalEvents.size());
        assertFalse(personalEvents.contains(allEvents.get(0)));
        assertFalse(personalEvents.contains(allEvents.get(1)));
        assertTrue(personalEvents.contains(allEvents.get(2)));
    }

    public void testGetRelatives() {
        List<Person> testRelatives = dataCache.getRelatives(user);
        assertTrue(testRelatives.contains(people.get(0)));
        assertTrue(testRelatives.contains(people.get(1)));
        assertTrue(testRelatives.contains(people.get(2)));
        assertFalse(testRelatives.contains(user));
    }

    public void testSortFamily() {
        dataCache.sortFamily();
        dataCache.setOptionToggle(MALE_EVENTS_TOGGLE, false);
        assertEquals(1, dataCache.getFilteredEvents().size());
        dataCache.setOptionToggle(FEMALE_EVENTS_TOGGLE, false);
        assertEquals(0, dataCache.getFilteredEvents().size());
        dataCache.setOptionToggle(MALE_EVENTS_TOGGLE, true);
        assertEquals(2, dataCache.getFilteredEvents().size());
        dataCache.setOptionToggle(FEMALE_EVENTS_TOGGLE, true);
        dataCache.setOptionToggle(MOTHER_SIDE_TOGGLE, false);
        assertEquals(3, dataCache.getFilteredEvents().size());
        dataCache.setOptionToggle(FATHER_SIDE_TOGGLE, false);
        assertEquals(2, dataCache.getFilteredEvents().size());
    }

    public void testGetOptionToggle() {
        for(int i = 0; i < 7; i++) {
            assertTrue(dataCache.getOptionToggle(i));
        }
    }

    public void testSetOptionToggle() {
        for(int i = 0; i < 7; i++) {
            dataCache.setOptionToggle(i, false);
        }
        for(int i = 0; i < 7; i++) {
            assertFalse(dataCache.getOptionToggle(i));
        }
    }

    public void testClear() {
        dataCache.clear();
        assertNull(dataCache.getAuthToken());
        assertNull(dataCache.getUser());
        assertEquals(0, dataCache.getAllPersons().size());
        assertEquals(0, dataCache.getAllEvents().size());
        assertEquals(0, dataCache.getFilteredEvents().size());
    }

    public void testSearchPeople() {
        assertEquals(2, dataCache.searchPeople("JOBS").size());
        assertEquals(4, dataCache.searchPeople("J").size());
        assertEquals(1, dataCache.searchPeople("NaNcY").size());
        assertEquals(1, dataCache.searchPeople("nc").size());
    }

    public void testSearchEvents() {
        assertEquals(3, dataCache.searchEvents("IMMi").size()); //Event_Type
        assertEquals(2, dataCache.searchEvents("LocAL").size()); //City
        assertEquals(1, dataCache.searchEvents("Nancy").size()); //First Name
        assertEquals(3, dataCache.searchEvents("J").size()); //First/Last Name
        assertEquals(3, dataCache.searchEvents("2").size()); //Year
        assertEquals(3, dataCache.searchEvents("anz").size()); //Country
    }
}