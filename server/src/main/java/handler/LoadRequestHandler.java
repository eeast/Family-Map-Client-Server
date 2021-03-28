package handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import Service.LoadService;
import request.LoadRequest;
import result.LoadResult;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class LoadRequestHandler implements HttpHandler {
    LoadService ls = new LoadService();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                System.out.println("Load request received");
                InputStream is = exchange.getRequestBody();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                Gson gsonDeserializer = new Gson();
                LoadRequest request = gsonDeserializer.fromJson(br, LoadRequest.class);
                LoadResult result = ls.load(request);
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
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
                System.out.println("Code: " + HttpURLConnection.HTTP_BAD_METHOD);
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
