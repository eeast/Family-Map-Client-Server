package handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import Service.FillService;
import request.FillRequest;
import result.FillResult;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class FillRequestHandler implements HttpHandler {
    FillService fs = new FillService();
    FillRequest request;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                System.out.println("Fill request received");
                String uri = exchange.getRequestURI().toString();
                String[] path = uri.split("/");
                if (path.length == 3 || path.length == 4) {
                    if (path.length == 3) {
                        request = new FillRequest(path[2]);
                    } else if (path.length == 4) {
                        int gen = Integer.parseInt(path[3]);
                        request = new FillRequest(path[2], gen);
                    }
                    FillResult result = fs.fill(request);
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
                }
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
