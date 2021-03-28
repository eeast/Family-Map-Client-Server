package com.example.myfamilymap.HTTPConnection;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import result.PersonResult;

public class PersonHandler extends ServerProxy {

    public PersonResult getUser(String urlString, String personID, String authToken) throws IOException {
        String urlRegister = urlString + "/person/" + personID;
        URL url = new URL(urlRegister);
        connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");

        connection.addRequestProperty("Authorization", authToken);

        connection.connect();

        PersonResult result = null;
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            Gson gsonDeserializer = new Gson();
            result = gsonDeserializer.fromJson(bufferedReader, PersonResult.class);
        } else {
            result = new PersonResult("Unable to obtain user information");
        }
        return result;
    }
}
