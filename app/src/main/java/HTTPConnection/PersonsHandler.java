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
import model.Person;
import result.PersonsResult;

public class PersonsHandler extends ServerProxy {

    /**
     * loadFamily function uses the current user's Person data to populate the Person
     * data for extended family members
     */
    public void loadFamily(String urlString) {
        DataCache dataCache = DataCache.getInstance();
        AuthToken authToken = dataCache.getAuthToken();
        ArrayList<Person> people = new ArrayList<Person>();
        System.out.println("Retrieving Persons via " + urlString);

        try {
            Handler handler = new Handler() {
                @Override
                public void handleMessage(@NonNull Message message) {
                    Bundle bundle = message.getData();
                    if (bundle.getBoolean(ARG_SUCCESS)) {
                        PersonsResult result = new Gson().fromJson(bundle.getString(ARG_DATA), PersonsResult.class);
                        DataCache dataCache = DataCache.getInstance();
                        for (Person person : result.getData()) {
                            dataCache.getAllPersons().add(person);
                        }
                    }
                }
            };

            LoadPersonsTask task = new LoadPersonsTask(handler, urlString, authToken.getToken());
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(task);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving Person data");
        }
    }

    /**
     * private Runnable class LoadPersonsTask uses PersonHandler class to connect to the server and
     * retrieve Person data for the current user's family.
     */
    private class LoadPersonsTask implements Runnable {
        private final Handler handler;
        private final String urlString;
        private final String authToken;

        /**
         * Constructor for LoadUserTask object
         * @param handler handler created during loadPerson method should be passed in
         * @param urlString String url for connection
         * @param authToken String authToken received during login/registration
         */
        public LoadPersonsTask(Handler handler, String urlString, String authToken) {
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
            PersonsResult personsResult = null;
            try {
                personsResult = accessServer(urlString, authToken);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error communicating with server");
            }

            sendMessage(personsResult);
        }

        /**
         * sendMessage method returns the success boolean, message, and Person data from the
         * PersonResult object back to the handler in the form of a bundle.
         * @param personsResult PersonsResult object received from the server
         */
        private void sendMessage(PersonsResult personsResult) {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(ARG_SUCCESS, personsResult.isSuccess());
            messageBundle.putString(ARG_MESSAGE, personsResult.getMessage());

            Gson gsonSerializer = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = gsonSerializer.toJson(personsResult);
            messageBundle.putString(ARG_DATA, jsonString);

            message.setData(messageBundle);

            handler.sendMessage(message);
        }
    }

    private PersonsResult accessServer(String urlString, String authToken) throws IOException {
        String urlRegister = urlString + "/person";
        URL url = new URL(urlRegister);
        connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");

        connection.addRequestProperty("Authorization", authToken);

        connection.connect();

        PersonsResult personsResult = null;
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            Gson gsonDeserializer = new Gson();
            personsResult = gsonDeserializer.fromJson(bufferedReader, PersonsResult.class);
        }

        return personsResult;
    }
}
