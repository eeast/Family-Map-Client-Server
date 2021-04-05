package HTTPConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class ServerProxy {
    protected HttpURLConnection connection;

    protected static final String ARG_SUCCESS = "Success";
    protected static final String ARG_MESSAGE = "Message";
    protected static final String ARG_AUTH_TOKEN = "AuthToken";
    protected static final String ARG_USERNAME = "Username";
    protected static final String ARG_PERSON_ID = "PersonID";
    protected static final String ARG_DATA = "JsonData";

    protected <R> void sendRequest(R request, OutputStream requestBody) throws IOException {
        Gson gsonSerializer = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gsonSerializer.toJson(request);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(requestBody));
        bufferedWriter.write(jsonString);
        bufferedWriter.flush();
    }

}
