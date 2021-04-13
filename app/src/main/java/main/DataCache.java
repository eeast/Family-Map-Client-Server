package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.AuthToken;
import model.Event;
import model.Person;

public class DataCache {
    private static DataCache instance;

    private final boolean[] optionToggle = new boolean[7];

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

    private static final int FATHER_SIDE_TOGGLE = 3;
    private static final int MOTHER_SIDE_TOGGLE = 4;
    private static final int MALE_EVENTS_TOGGLE = 5;
    private static final int FEMALE_EVENTS_TOGGLE = 6;

    public static synchronized DataCache getInstance() {
        if(instance == null) {
            instance = new DataCache();
            Arrays.fill(instance.optionToggle, true);
        }
        return instance;
    }

    private DataCache() {
        //required empty constructor
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public Set<Event> getFilteredEvents() {
        Set<Event> result = new HashSet<>();
        Set<String> currentPeople = getFilteredPeopleIDs();
        for(Event e : allEvents) {
            if(currentPeople.contains(e.getPerson_id())) {
                result.add(e);
            }
        }
        return result;
    }

    public Set<Person> getFilteredPersons() {
        Set<Person> result = new HashSet<>();
        if(optionToggle[MALE_EVENTS_TOGGLE]) {
            if("m".equals(user.getGender())) {
                result.add(user);
            }
            if(optionToggle[FATHER_SIDE_TOGGLE]) {
                result.addAll(fatherSideMales);
            }
            if(optionToggle[MOTHER_SIDE_TOGGLE]) {
                result.addAll(motherSideMales);
            }
        }
        if(optionToggle[FEMALE_EVENTS_TOGGLE]) {
            if("f".equals(user.getGender())) {
                result.add(user);
            }
            if (optionToggle[FATHER_SIDE_TOGGLE]) {
                result.addAll(fatherSideFemales);
            }
            if (optionToggle[MOTHER_SIDE_TOGGLE]) {
                result.addAll(motherSideFemales);
            }
        }
        return result;
    }

    private Set<String> getFilteredPeopleIDs() {
        Set<String> result = new HashSet<>();
        if(optionToggle[MALE_EVENTS_TOGGLE]) {
            if(user.getGender().equals("m")) {
                result.add(user.getPerson_id());
            }
            if(optionToggle[FATHER_SIDE_TOGGLE]) {
                for(Person p : fatherSideMales) {
                    result.add(p.getPerson_id());
                }
            }
            if(optionToggle[MOTHER_SIDE_TOGGLE]) {
                for(Person p : motherSideMales) {
                    result.add(p.getPerson_id());
                }
            }
        }
        if(optionToggle[FEMALE_EVENTS_TOGGLE]) {
            if(user.getGender().equals("f")) {
                result.add(user.getPerson_id());
            }
            if (optionToggle[FATHER_SIDE_TOGGLE]) {
                for(Person p : fatherSideFemales) {
                    result.add(p.getPerson_id());
                }
            }
            if (optionToggle[MOTHER_SIDE_TOGGLE]) {
                for(Person p : motherSideFemales) {
                    result.add(p.getPerson_id());
                }
            }
        }
        return result;
    }

    public Set<Event> getAllEvents() {
        return allEvents;
    }

    public Set<Person> getAllPersons() {
        return allPersons;
    }

    public Person findPerson(String personID) {
        Set<String> filteredPersons = getFilteredPeopleIDs();
        for(Person person : allPersons) {
            if (person.getPerson_id().equals(personID) && filteredPersons.contains(person.getPerson_id())) {
                return person;
            }
        }
        return null;
    }

    public Event findEvent(String eventID) {
        Set<String> filteredPersons = getFilteredPeopleIDs();
        for(Event event : allEvents) {
            if (event.getEvent_id().equals(eventID) && filteredPersons.contains(event.getPerson_id())) {
                return event;
            }
        }
        return null;
    }

    public List<Event> getPersonalEvents(String personID) {
        List<Event> events = new ArrayList<>();
        if(getFilteredPeopleIDs().contains(personID)) {
            for (Event event : allEvents) {
                if (event.getPerson_id().equals(personID)) {
                    for (int i = 0; i < events.size(); i++) {
                        if (events.get(i).getYear() > event.getYear()) {
                            events.add(i, event);
                            break;
                        } else if (events.get(i).getYear() == event.getYear()) {
                            if (events.get(i).getEvent_type().toLowerCase().compareTo(event.getEvent_type().toLowerCase()) > 0) {
                                events.add(i, event);
                                break;
                            }
                            events.add(i + 1, event);
                            break;
                        }
                    }
                    if (!events.contains(event)) {
                        events.add(event);
                    }
                }
            }
        }
        return events;
    }

    public List<Person> getRelatives(Person person) {
        List<Person> relatives = new ArrayList<>();
        Set<String> filteredPersons = getFilteredPeopleIDs();
        if(user.getPerson_id().equals(person.getPerson_id())) {
            for(Person currentPerson : immediateFamilyFemales) {
                if(filteredPersons.contains(currentPerson.getPerson_id())) {
                    if (currentPerson.getPerson_id().equals(person.getFather_id())) { //father
                        relatives.add(currentPerson);
                    } else if (currentPerson.getPerson_id().equals(person.getMother_id())) { //mother
                        relatives.add(currentPerson);
                    } else if (currentPerson.getPerson_id().equals(person.getSpouse_id())) { //spouse
                        relatives.add(currentPerson);
                    } else if (currentPerson.getMother_id() != null && currentPerson.getMother_id().equals(person.getPerson_id())) { //child
                        relatives.add(currentPerson);
                    } else if (currentPerson.getFather_id() != null && currentPerson.getFather_id().equals(person.getPerson_id())) { //child
                        relatives.add(currentPerson);
                    }
                }
            }
            for(Person currentPerson : immediateFamilyMales) {
                if(filteredPersons.contains(currentPerson.getPerson_id())) {
                    if (currentPerson.getPerson_id().equals(person.getFather_id())) { //father
                        relatives.add(currentPerson);
                    } else if (currentPerson.getPerson_id().equals(person.getMother_id())) { //mother
                        relatives.add(currentPerson);
                    } else if (currentPerson.getPerson_id().equals(person.getSpouse_id())) { //spouse
                        relatives.add(currentPerson);
                    } else if (currentPerson.getMother_id() != null && currentPerson.getMother_id().equals(person.getPerson_id())) { //child
                        relatives.add(currentPerson);
                    } else if (currentPerson.getFather_id() != null && currentPerson.getFather_id().equals(person.getPerson_id())) { //child
                        relatives.add(currentPerson);
                    }
                }
            }
        } else {
            for (Person currentPerson : allPersons) {
                if (filteredPersons.contains(currentPerson.getPerson_id())) {
                    if (currentPerson.getPerson_id().equals(person.getFather_id())) { //father
                        relatives.add(currentPerson);
                    } else if (currentPerson.getPerson_id().equals(person.getMother_id())) { //mother
                        relatives.add(currentPerson);
                    } else if (currentPerson.getPerson_id().equals(person.getSpouse_id())) { //spouse
                        relatives.add(currentPerson);
                    } else if (currentPerson.getMother_id() != null && currentPerson.getMother_id().equals(person.getPerson_id())) { //child
                        relatives.add(currentPerson);
                    } else if (currentPerson.getFather_id() != null && currentPerson.getFather_id().equals(person.getPerson_id())) { //child
                        relatives.add(currentPerson);
                    }
                }
            }
        }
        return relatives;
    }

    public void sortFamily() {
        for (Person person : allPersons) {
            //immediate family
            if (person.getPerson_id().equals(user.getMother_id())) {
                immediateFamilyFemales.add(person);
                motherSideFemales.add(person);
                loadParents(person, true);
            } else if (person.getPerson_id().equals(user.getFather_id())) {
                immediateFamilyMales.add(person);
                fatherSideMales.add(person);
                loadParents(person, false);
            } else if (person.getPerson_id().equals(user.getSpouse_id())) {
                if (person.getGender().equals("m")) {
                    immediateFamilyMales.add(person);
                } else {
                    immediateFamilyFemales.add(person);
                }
            }
        }

    }

    private void loadParents(Person child, Boolean motherSide) {
        if (child.getMother_id() == null && child.getFather_id() == null) {
            return;
        }
        for (Person person : allPersons) {
            if (person.getPerson_id().equals(child.getFather_id())) {
                if (motherSide) {
                    motherSideMales.add(person);
                } else {
                    fatherSideMales.add(person);
                }
                loadParents(person, motherSide);
            } else if (person.getPerson_id().equals(child.getMother_id())){
                if(motherSide) {
                    motherSideFemales.add(person);
                } else {
                    fatherSideFemales.add(person);
                }
                loadParents(person, motherSide);
            }
        }
    }

    public boolean getOptionToggle(int index) {
        return optionToggle[index];
    }

    public void setOptionToggle(int index, boolean value) {
        optionToggle[index] = value;
    }

    public void clear() {
        authToken = null;

        user = null;

        allPersons.clear();
        allEvents.clear();

        immediateFamilyMales.clear();
        immediateFamilyFemales.clear();

        fatherSideMales.clear();
        fatherSideFemales.clear();
        motherSideMales.clear();
        motherSideFemales.clear();

        Arrays.fill(optionToggle, true);
    }
}
