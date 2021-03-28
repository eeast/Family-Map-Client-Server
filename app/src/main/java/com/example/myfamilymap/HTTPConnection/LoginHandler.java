package com.example.myfamilymap.HTTPConnection;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import request.LoginRequest;
import result.LoginResult;

public class LoginHandler extends ServerProxy {

    public LoginResult login(String urlString, LoginRequest request) throws IOException {
        String urlLogin = urlString + "/user/login";
        URL url = new URL(urlLogin);
        connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.connect();

        try (OutputStream requestBody = connection.getOutputStream();) {
            sendRequest(request, requestBody);
        }

        LoginResult result = null;
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            Gson gsonDeserializer = new Gson();
            result = gsonDeserializer.fromJson(bufferedReader, LoginResult.class);
            return result;
        } else {
            result = new LoginResult("Unable to authenticate");
            return result;
        }
    }
}
