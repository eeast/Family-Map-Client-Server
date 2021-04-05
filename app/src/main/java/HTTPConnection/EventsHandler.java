package HTTPConnection;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.DataCache;
import model.AuthToken;
import model.Event;
import result.EventsResult;

public class EventsHandler extends ServerProxy{

    /**
     * loadFamily function uses the current user's Person data to populate the Person
     * data for extended family members
     */
    public void loadFamilyEvents(String urlString) {
        DataCache dataCache = DataCache.getInstance();
        AuthToken authToken = dataCache.getAuthToken();
        ArrayList<Event> events = new ArrayList<Event>();
        System.out.println("Retrieving Events via " + urlString);

        try {
            Handler handler = new Handler() {
                @Override
                public void handleMessage(@NonNull Message message) {
                    Bundle bundle = message.getData();
                    if (bundle.getBoolean(ARG_SUCCESS)) {
                        EventsResult result = new Gson().fromJson(bundle.getString(ARG_DATA), EventsResult.class);
                        DataCache dataCache = DataCache.getInstance();
                        for (Event event : result.getData()) {
                            dataCache.getAllEvents().add(event);
                        }
                    }
                }
            };

            LoadEventsTask task = new LoadEventsTask(handler, urlString, authToken.getToken());
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(task);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving Person data");
        }
    }

    /**
     * private Runnable class LoadEventsTask uses EventsHandler class to connect to the server and
     * retrieve Event data for the current user's family.
     */
    private class LoadEventsTask implements Runnable {
        private final Handler handler;
        private final String urlString;
        private final String authToken;

        /**
         * Constructor for LoadUserTask object
         * @param handler handler created during loadPerson method should be passed in
         * @param urlString String url for connection
         * @param authToken String authToken received during login/registration
         */
        public LoadEventsTask(Handler handler, String urlString, String authToken) {
            this.handler = handler;
            this.urlString = urlString;
            this.authToken = authToken;
        }

        /**
         * Override of run method: creates PersonHandler object, receives the PersonResult object
         * from the server, converts the result into a Person object, and adds the Person object
         * data to the main.DataCache.
         */
        @Override
        public void run() {
            EventsResult eventsResult = null;
            try {
                eventsResult = accessServer(urlString, authToken);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error communicating with server");
            }

            sendMessage(eventsResult);
        }

        /**
         * sendMessage method returns the success boolean, message, and Person data from the
         * PersonResult object back to the handler in the form of a bundle.
         * @param eventsResult PersonsResult object received from the server
         */
        private void sendMessage(EventsResult eventsResult) {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(ARG_SUCCESS, eventsResult.isSuccess());
            messageBundle.putString(ARG_MESSAGE, eventsResult.getMessage());

            Gson gsonSerializer = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = gsonSerializer.toJson(eventsResult);
            messageBundle.putString(ARG_DATA, jsonString);

            message.setData(messageBundle);

            handler.sendMessage(message);
        }
    }

    private EventsResult accessServer(String urlString, String authToken) throws IOException {
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
