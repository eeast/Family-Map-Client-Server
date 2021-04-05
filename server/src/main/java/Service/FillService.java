package Service;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import data_access.EventDAO;
import data_access.PersonDAO;
import data_access.UserDAO;
import model.Event;
import model.FemaleNames;
import model.Location;
import model.Locations;
import model.MaleNames;
import model.Person;
import model.Surnames;
import model.User;
import request.FillRequest;
import result.FillResult;

/**
 * Fill extension of the Service class. Contains the function and logic for producing "realistic" family history data
 * for a given user.
 */
public class FillService extends Service {
    Random random = new Random();

    /**
     * Populates the server's database with generated data for the specified user name. The required "username"
     * parameter must be a user already registered with the server. If there is any data in the database already
     * associated with the given user name, it is deleted. The optional generations parameter lets the caller
     * specify the number of generations of ancestors to be generated, and must be a non-negative integer (the
     * default is 4, which results in 31 new persons each with associated events).
     * @param r the FillRequest object with the required parameters
     * @return FillResult object with a boolean success identifier, and if successful, a String message identifying
     * the number of Persons and Events added to the database. If unsuccessful, a String error message
     * is included identifying the issue.
     */
    public FillResult fill(FillRequest r) {
        try {
            Connection conn = db.getConnection();
            eDAO = new EventDAO(conn);
            pDAO = new PersonDAO(conn);
            uDAO = new UserDAO(conn);
            if (eDAO.getEvents(r.getUsername()).size() > 0 || pDAO.getPersons(r.getUsername()).size() > 0) {
                db.clearData(r.getUsername());
            }
            User user = uDAO.getUser(r.getUsername());
            if (user != null && r.getGenerations() > 0) {
                ArrayList<Person> people = new ArrayList<Person>();
                Person uPerson = generateUserPerson(user);
                people.add(uPerson);
                Event uBirth = generateUserBirth(uPerson);
                eDAO.insertEvent(uBirth);
                int numGens = r.getGenerations();
                ArrayList<Person> newPeople = new ArrayList<Person>();
                int eventCount = 1;
                for (int gen = 0; gen < numGens; gen++) {
                    newPeople = new ArrayList<Person>();
                    for (Person person : people) {
                        if (person.getFather_id() == null) {
                            Event childBirth = eDAO.getEventType(person.getPerson_id(), "birth");

                            //create father Person, and marriage, birth, and death Events
                            Person father = generateFather(person);
                            Event marriageFather = generateMarriage(father, childBirth.getYear());
                            Event deathFather = generateDeath(father, childBirth.getYear());
                            Event birthFather = generateBirth(father, marriageFather.getYear());
                            newPeople.add(father);
                            eDAO.insertEvent(marriageFather);
                            eDAO.insertEvent(deathFather);
                            eDAO.insertEvent(birthFather);

                            //create mother Person, and marriage, birth, and death Events
                            Person mother = generateMother(father);
                            Event marriageMother = duplicateMarriage(mother, marriageFather);
                            Event deathMother = generateDeath(mother, childBirth.getYear());
                            Event birthMother = generateBirth(mother, marriageMother.getYear());
                            newPeople.add(mother);
                            eDAO.insertEvent(marriageMother);
                            eDAO.insertEvent(deathMother);
                            eDAO.insertEvent(birthMother);

                            eventCount += 6;
                        }
                    }
                    for (Person p : newPeople) {
                        people.add(p);
                    }
                }
                for (Person p : people) {
                    pDAO.insertPerson(p);
                }
                db.closeConnection(true);
                return new FillResult("Successfully added " + people.size() + " persons and " + eventCount +
                        " events to the database.", true);
            } else {
                db.closeConnection(false);
                return new FillResult("Invalid username or generations parameter", false);
            }
        } catch (Exception e) {
            try {
                db.closeConnection(false);
            } catch (Exception ex) {}
        }
        return new FillResult("Internal server error", false);
    }

    private Person generateUserPerson(User u) {
        String person_id = u.getPerson_id();
        String assoc_username = u.getUsername();
        String first_name = u.getFirst_name();
        String last_name = u.getLast_name();
        String gender = u.getGender();
        return new Person (person_id, assoc_username, first_name, last_name, gender, null, null, null);
    }

    private Person generateFather(Person child) {
        String person_id = UUID.randomUUID().toString();
        String assoc_username = child.getAssoc_username();
        String first_name = getMaleFirstName();
        String last_name;
        if (child.getGender().equals("m")) {
            last_name = child.getLast_name();
        } else {
            last_name = getSurname();
        }
        String gender = "m";
        String spouse_id = UUID.randomUUID().toString();

        child.setFather_id(person_id);
        child.setMother_id(spouse_id);

        Person p = new Person (person_id, assoc_username, first_name, last_name, gender);
        p.setSpouse_id(spouse_id);
        return p;
    }

    private Person generateMother(Person spouse) {
        String person_id = spouse.getSpouse_id();
        String assoc_username = spouse.getAssoc_username();
        String first_name = getFemaleFirstName();
        String last_name = spouse.getLast_name();
        String gender = "f";

        Person p = new Person (person_id, assoc_username, first_name, last_name, gender);
        p.setSpouse_id(spouse.getPerson_id());
        return p;
    }

    private Event generateUserBirth(Person p) {
        String event_id = UUID.randomUUID().toString();
        String assoc_username = p.getAssoc_username();
        String person_id = p.getPerson_id();
        Location l = getLocation();
        float latitude = l.getLatitude();
        float longitude = l.getLongitude();
        String country = l.getCountry();
        String city = l.getCity();
        String event_type = "birth";
        int year = (2021 - 18 - random.nextInt(15));
        return new Event (event_id, assoc_username, person_id, latitude, longitude, country, city, event_type, year);
    }

    private Event generateBirth(Person p, int limit) {
        String event_id = UUID.randomUUID().toString();
        String assoc_username = p.getAssoc_username();
        String person_id = p.getPerson_id();
        Location l = getLocation();
        float latitude = l.getLatitude();
        float longitude = l.getLongitude();
        String country = l.getCountry();
        String city = l.getCity();
        String event_type = "birth";
        int year = (limit - 18 - random.nextInt(11));
        return new Event (event_id, assoc_username, person_id, latitude, longitude, country, city, event_type, year);
    }

    private Event generateDeath(Person p, int limit) {
        String event_id = UUID.randomUUID().toString();
        String assoc_username = p.getAssoc_username();
        String person_id = p.getPerson_id();
        Location l = getLocation();
        float latitude = l.getLatitude();
        float longitude = l.getLongitude();
        String country = l.getCountry();
        String city = l.getCity();
        String event_type = "death";
        int year = (limit + random.nextInt(51) + 10);
        while (year > 2021) {
            year = (limit + random.nextInt(51) + 10);
        }
        return new Event (event_id, assoc_username, person_id, latitude, longitude, country, city, event_type, year);
    }

    private Event generateMarriage(Person p, int limit) {
        String event_id = UUID.randomUUID().toString();
        String assoc_username = p.getAssoc_username();
        String person_id = p.getPerson_id();
        Location l = getLocation();
        float latitude = l.getLatitude();
        float longitude = l.getLongitude();
        String country = l.getCountry();
        String city = l.getCity();
        String event_type = "marriage";
        int year = (limit - random.nextInt(10) - 1);
        return new Event (event_id, assoc_username, person_id, latitude, longitude, country, city, event_type, year);
    }

    private Event duplicateMarriage(Person p, Event e) {
        String event_id = UUID.randomUUID().toString();
        String assoc_username = p.getAssoc_username();
        String person_id = p.getPerson_id();
        float latitude = e.getLatitude();
        float longitude = e.getLongitude();
        String country = e.getCountry();
        String city = e.getCity();
        String event_type = "marriage";
        int year = e.getYear();
        return new Event (event_id, assoc_username, person_id, latitude, longitude, country, city, event_type, year);
    }

    private Location getLocation() {
        Location l = null;
        try {
            Gson gson = new Gson();
            BufferedReader br = Files.newBufferedReader(Paths.get("server/json/locations.json"));
            Locations locations = gson.fromJson(br, Locations.class);
            br.close();
            int index = random.nextInt(locations.size());
            l = locations.getData().get(index);
        } catch (Exception e) {
            System.out.println("ERROR loading location file");
        }
        return l;
    }

    private String getMaleFirstName() {
        String n = null;
        try {
            Gson gson = new Gson();
            BufferedReader br = Files.newBufferedReader(Paths.get("server/json/mnames.json"));
            MaleNames maleNames = gson.fromJson(br, MaleNames.class);
            br.close();
            int index = random.nextInt(maleNames.size());
            n = maleNames.getData().get(index);
        } catch (Exception e) {
            System.out.println("ERROR loading location file");
        }
        return n;
    }

    private String getSurname() {
        String n = null;
        try {
            Gson gson = new Gson();
            BufferedReader br = Files.newBufferedReader(Paths.get("server/json/snames.json"));
            Surnames surnames = gson.fromJson(br, Surnames.class);
            br.close();
            int index = random.nextInt(surnames.size());
            n = surnames.getData().get(index);
        } catch (Exception e) {
            System.out.println("ERROR loading location file");
        }
        return n;
    }

    private String getFemaleFirstName() {
        String n = null;
        try {
            Gson gson = new Gson();
            BufferedReader br = Files.newBufferedReader(Paths.get("server/json/fnames.json"));
            FemaleNames femaleNames = gson.fromJson(br, FemaleNames.class);
            br.close();
            int index = random.nextInt(femaleNames.size());
            n = femaleNames.getData().get(index);
        } catch (Exception e) {
            System.out.println("ERROR loading location file");
        }
        return n;
    }
}
