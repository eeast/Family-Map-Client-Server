package FMServer;

import com.sun.net.httpserver.HttpServer;
import handler.*;

import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;

public class FamilyMapServer {
    public static void main(String[] args) {
        try{
            startServer(8080);
        } catch (Exception e) {}
    }

    private static void startServer (int port) throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress(port);
        HttpServer server = HttpServer.create(serverAddress, 10);
        registerHandlers(server);
        server.start();
        System.out.println("FamilyMapServer listening on port " + port);
    }

    private static void registerHandlers(HttpServer server) {
        server.createContext("/", new DefaultHandler());
        server.createContext("/user/register", new RegisterRequestHandler());
        server.createContext("/user/login", new LoginRequestHandler());
        server.createContext("/clear", new ClearRequestHandler());
        server.createContext("/fill/", new FillRequestHandler());
        server.createContext("/load", new LoadRequestHandler());
        server.createContext("/person/", new PersonRequestHandler());
        server.createContext("/person", new PersonsRequestHandler());
        server.createContext("/event/", new EventRequestHandler());
        server.createContext("/event", new EventsRequestHandler());
    }

}
