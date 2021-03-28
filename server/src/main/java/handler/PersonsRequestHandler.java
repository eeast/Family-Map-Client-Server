package handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import Service.AuthorizationService;
import Service.PersonsService;
import result.AuthorizationResult;
import result.PersonsResult;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class PersonsRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        AuthorizationService as = new AuthorizationService();
        PersonsService ps = new PersonsService();

        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                System.out.println("Persons request received");
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");
                    AuthorizationResult tokenResult = as.authorize(authToken);
                    if (tokenResult.isSuccess()) {
                        PersonsResult result = ps.persons(tokenResult.getUsername());
                        if (result.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                            System.out.println("Code: " + HttpURLConnection.HTTP_OK);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                            System.out.println("Code: " + HttpURLConnection.HTTP_BAD_REQUEST);
                        }
                        OutputStream os = exchange.getResponseBody();
                        Gson gsonSerializer = new GsonBuilder().setPrettyPrinting().create();
                        String jsonString = gsonSerializer.toJson(result);
                        System.out.println(jsonString);
                        writeString(jsonString, os);
                    } else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        System.out.println("Code: " + HttpURLConnection.HTTP_BAD_REQUEST);
                        OutputStream os = exchange.getResponseBody();
                        Gson gsonSerializer = new GsonBuilder().setPrettyPrinting().create();
                        String jsonString = gsonSerializer.toJson(tokenResult);
                        System.out.println(jsonString);
                        writeString(jsonString, os);
                    }
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                    System.out.println("Code: " + HttpURLConnection.HTTP_UNAUTHORIZED);
                }
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                System.out.println("Code: " + HttpURLConnection.HTTP_BAD_REQUEST);
            }
            exchange.close();
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_IMPLEMENTED, 0);
            System.out.println("Code: " + HttpURLConnection.HTTP_NOT_IMPLEMENTED);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
    }
}
