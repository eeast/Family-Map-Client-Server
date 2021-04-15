package dataManagement;

import junit.framework.TestCase;

import model.Person;
import request.LoginRequest;
import request.RegisterRequest;
import result.EventsResult;
import result.LoginResult;
import result.PersonResult;
import result.PersonsResult;
import result.RegisterResult;

import static org.junit.Assert.assertNotEquals;

public class ServerProxyTest extends TestCase {
    ServerProxy serverProxy = new ServerProxy();
    String urlString = "http://localhost:8080";
    String username = "user667";
    String password = "password";
    String email = "email";
    String firstName = "John";
    String lastName = "Paul";
    String gender = "m";

    String username2 = "otherUserMike";

    String authToken;
    String personID;

    public void setUp() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(username, password, email, firstName, lastName, gender);
        RegisterResult registerResult = serverProxy.register(urlString, registerRequest);
        if(registerResult.isSuccess()) {
            authToken = registerResult.getAuth_token();
            personID = registerResult.getPerson_id();
        } else {
            LoginRequest loginRequest = new LoginRequest(username, password);
            LoginResult loginResult = serverProxy.login(urlString, loginRequest);
            authToken = loginResult.getAuthtoken();
            personID = loginResult.getPerson_id();
        }
    }

    public void testRegister() {
        RegisterRequest registerRequest = new RegisterRequest(username2, password, "email", "Mike", "Wazowski", "m");
        RegisterResult registerResult = serverProxy.register(urlString, registerRequest);
        assertTrue(registerResult.isSuccess());
        assertEquals(username2, registerResult.getUsername());
        assertNotEquals(username, registerResult.getUsername());
        assertNotEquals(personID, registerResult.getPerson_id());
    }

    public void testRegisterFail() {
        RegisterRequest registerRequest = new RegisterRequest(username, password, "email", "Mike", "Wazowski", "m");
        RegisterResult registerResult = serverProxy.register(urlString, registerRequest);
        assertFalse(registerResult.isSuccess());
        assertNull(registerResult.getUsername());
        assertNull(registerResult.getUsername());
        assertNull(registerResult.getPerson_id());
    }

    public void testLogin() {
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = serverProxy.login(urlString, loginRequest);
        assertTrue(loginResult.isSuccess());
        assertEquals(username, loginResult.getUsername());
        assertNotEquals(authToken, loginResult.getAuthtoken());
        assertEquals(personID, loginResult.getPerson_id());
    }

    public void testLoadUser() {
        PersonResult personResult = serverProxy.loadUser(urlString, personID);
        assertTrue(personResult.isSuccess());
        assertEquals(firstName, personResult.getFirst_name());
        assertEquals(lastName, personResult.getLast_name());
        assertEquals(gender, personResult.getGender());
        assertNotNull(personResult.getFather_id());
        assertNotNull(personResult.getMother_id());
    }

    public void testLoadPersons() {
        PersonResult personResult = serverProxy.loadUser(urlString, personID);
        Person user = new Person(personResult.getPerson_id(),
                personResult.getAssoc_username(),
                personResult.getFirst_name(),
                personResult.getLast_name(),
                personResult.getGender(),
                personResult.getFather_id(),
                personResult.getMother_id(),
                personResult.getSpouse_id());
        PersonsResult personsResult = serverProxy.loadPersons(urlString, authToken);
        assertTrue(personsResult.isSuccess());
        assertEquals(31, personsResult.getData().size());
        assertTrue(personsResult.getData().contains(user));
    }

    public void testLoadEvents() {
        EventsResult eventsResult = serverProxy.loadEvents(urlString, authToken);
        assertTrue(eventsResult.isSuccess());
        assertEquals(91, eventsResult.getData().size());
    }
}