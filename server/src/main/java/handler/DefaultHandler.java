package handler;

import com.sun.net.httpserver.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.file.Files;

public class DefaultHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                System.out.println("Landing page accessed");
                Headers reqHeaders = exchange.getRequestHeaders();
                String urlPath = exchange.getRequestURI().toString();
                if (urlPath.equals("") || urlPath.equals("/")) {
                    urlPath = "/index.html";
                }
                String filePath = "server/web" + urlPath;
                File file = new File(filePath);
                if (file.exists()) {
                    System.out.println("File found");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(file.toPath(), respBody);
                } else {
                    System.out.println("File not found");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    String errorPath = "web/HTML/404.html";
                    File errorFile = new File(errorPath);
                    if (errorFile.exists()) {
                        OutputStream respBody = exchange.getResponseBody();
                        Files.copy(errorFile.toPath(), respBody);
                    }
                }
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
            exchange.close();
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_IMPLEMENTED, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
