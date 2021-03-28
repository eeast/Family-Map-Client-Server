package handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import Service.ClearService;
import result.ClearResult;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class ClearRequestHandler implements HttpHandler {
    ClearService cs = new ClearService();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                System.out.println("Clear request received");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                System.out.println("Code: " + HttpURLConnection.HTTP_OK);
                ClearResult result = cs.clearTables();
                OutputStream os = exchange.getResponseBody();
                Gson gsonSerializer = new GsonBuilder().setPrettyPrinting().create();
                String jsonString = gsonSerializer.toJson(result);
                System.out.println(jsonString);
                writeString(jsonString, os);
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
