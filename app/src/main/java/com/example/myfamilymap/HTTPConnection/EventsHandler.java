package com.example.myfamilymap.HTTPConnection;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import result.EventsResult;

public class EventsHandler extends ServerProxy{

    public EventsResult getEvents(String urlString, String authToken) throws IOException {
        String urlRegister = urlString + "/event";
        URL url = new URL(urlRegister);
        connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");

        connection.addRequestProperty("Authorization", authToken);

        connection.connect();

        EventsResult eventsResult = null;
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            Gson gsonDeserializer = new Gson();
            eventsResult = gsonDeserializer.fromJson(bufferedReader, EventsResult.class);
        }

        return eventsResult;
    }
}
