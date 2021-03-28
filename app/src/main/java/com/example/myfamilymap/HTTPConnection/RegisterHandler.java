package com.example.myfamilymap.HTTPConnection;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import request.RegisterRequest;
import result.RegisterResult;

public class RegisterHandler extends ServerProxy {

    public RegisterResult register(String urlString, RegisterRequest request) throws IOException {
        String urlRegister = urlString + "/user/register";
        URL url = new URL(urlRegister);
        connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.connect();

        try (OutputStream requestBody = connection.getOutputStream();) {
            sendRequest(request, requestBody);
        }

        RegisterResult result = null;
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            Gson gsonDeserializer = new Gson();
            result = gsonDeserializer.fromJson(bufferedReader, RegisterResult.class);
        } else {
            result = new RegisterResult("Unable to complete registration");
        }
        return result;
    }
}
