package dataManagement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import model.AuthToken;
import model.Person;
import request.LoginRequest;
import request.RegisterRequest;
import result.EventsResult;
import result.LoginResult;
import result.PersonResult;
import result.PersonsResult;
import result.RegisterResult;

public class ServerProxy {
    protected HttpURLConnection connection;

    private <R> void sendRequest(R request, OutputStream requestBody) throws IOException {
        Gson gsonSerializer = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gsonSerializer.toJson(request);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(requestBody));
        bufferedWriter.write(jsonString);
        bufferedWriter.flush();
    }

    public RegisterResult register(String urlString, RegisterRequest registerRequest) {
        RegisterResult registerResult = null;
        if (registerRequest != null){
            try {
                String urlRegister = urlString + "/user/register";
                URL url = new URL(urlRegister);
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(5000);
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.connect();

                try (OutputStream requestBody = connection.getOutputStream();) {
                    sendRequest(registerRequest, requestBody);
                }

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    Gson gsonDeserializer = new Gson();
                    registerResult = gsonDeserializer.fromJson(bufferedReader, RegisterResult.class);
                } else {
                    registerResult = new RegisterResult("Unable to complete registration");
                }

                if(registerResult.isSuccess()) {
                    DataCache dataCache = DataCache.getInstance();
                    dataCache.setAuthToken(new AuthToken(registerResult.getAuth_token(), registerResult.getUsername()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error communicating with server");
                registerResult = new RegisterResult("Error communication with server");
            }
        }
        return registerResult;
    }

    public LoginResult login(String urlString, LoginRequest loginRequest) {
        LoginResult loginResult = null;
        if (loginRequest != null){
            try {
                String urlLogin = urlString + "/user/login";
                URL url = new URL(urlLogin);
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(5000);
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.connect();

                try (OutputStream requestBody = connection.getOutputStream();) {
                    sendRequest(loginRequest, requestBody);
                }

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    Gson gsonDeserializer = new Gson();
                    loginResult = gsonDeserializer.fromJson(bufferedReader, LoginResult.class);
                } else {
                    loginResult = new LoginResult("Unable to authenticate");
                }

                if(loginResult.isSuccess()) {
                    DataCache dataCache = DataCache.getInstance();
                    dataCache.setAuthToken(new AuthToken(loginResult.getAuthtoken(), loginResult.getUsername()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error communicating with server");
                loginResult = new LoginResult("Error communicating with server");
            }
        }

        return loginResult;
    }

    public PersonResult loadUser (String urlString, String personID) {
        DataCache dataCache = DataCache.getInstance();
        String authToken = dataCache.getAuthToken().getToken();
        PersonResult personResult = null;
        try {
            String urlRegister = urlString + "/person/" + personID;
            URL url = new URL(urlRegister);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");

            connection.addRequestProperty("Authorization", authToken);

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                Gson gsonDeserializer = new Gson();
                personResult = gsonDeserializer.fromJson(bufferedReader, PersonResult.class);
            } else {
                personResult = new PersonResult("Unable to obtain user information");
            }
            if (personResult.isSuccess()) {
                Person person = new Person(
                        personResult.getPerson_id(),
                        personResult.getAssoc_username(),
                        personResult.getFirst_name(),
                        personResult.getLast_name(),
                        personResult.getGender(),
                        personResult.getFather_id(),
                        personResult.getMother_id(),
                        personResult.getSpouse_id()
                );
                dataCache.setUser(person);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error communicating with server");
            personResult = new PersonResult("Error communicating with server");
        }

        return personResult;
    }

    public PersonsResult loadPersons(String urlString, String authToken) {
        PersonsResult personsResult = null;
        try {
            String urlRegister = urlString + "/person";
            URL url = new URL(urlRegister);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");

            connection.addRequestProperty("Authorization", authToken);

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                Gson gsonDeserializer = new Gson();
                personsResult = gsonDeserializer.fromJson(bufferedReader, PersonsResult.class);
            }

            if (personsResult.isSuccess()) {
                DataCache dataCache = DataCache.getInstance();
                dataCache.getAllPersons().addAll(personsResult.getData());
                dataCache.sortFamily();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error communicating with server");
            personsResult = new PersonsResult("Error communicating with server");
        }

        return personsResult;
    }

    public EventsResult loadEvents(String urlString, String authToken) {
        EventsResult eventsResult = null;
        try {
            String urlRegister = urlString + "/event";
            URL url = new URL(urlRegister);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");

            connection.addRequestProperty("Authorization", authToken);

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                Gson gsonDeserializer = new Gson();
                eventsResult = gsonDeserializer.fromJson(bufferedReader, EventsResult.class);
            }

            if (eventsResult.isSuccess()) {
                DataCache dataCache = DataCache.getInstance();
                dataCache.getAllEvents().addAll(eventsResult.getData());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error communicating with server");
            eventsResult = new EventsResult("Error communicating with server");
        }

        return eventsResult;
    }


}
