package HTTPConnection;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.DataCache;
import model.Person;
import result.PersonResult;

public class UserHandler extends ServerProxy {

    /**
     * loadUser function creates handler and executor for LoadUserTask
     * @param personID String personID received upon login/registration
     */
    public void loadUser(View view, String urlString, String personID) {
        System.out.println("Retrieving User via " + urlString);

        try {
            Handler handler = new Handler() {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    DataCache dataCache = DataCache.getInstance();
                    Toast.makeText(view.getContext(), String.format("Welcome, %s %s!",
                            dataCache.getUser().getFirst_name(), dataCache.getUser().getLast_name()),
                            Toast.LENGTH_LONG).show();
                }
            };

            DataCache dataCache = DataCache.getInstance();
            String authToken = dataCache.getAuthToken().getToken();
            LoadUserTask task = new LoadUserTask(handler, urlString, personID, authToken);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(task);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving Person Data for User");
        }
    }

    /**
     * protected Runnable class LoadUserTask uses PersonHandler class to connect to the server and
     * retrieve Person data for the current user.
     */
    protected class LoadUserTask implements Runnable {
        private final Handler handler;
        private final String urlString;
        private final String personID;
        private final String authToken;

        /**
         * Constructor for LoadUserTask object
         * @param handler handler created during loadUser method should be passed in
         * @param urlString String url for connection
         * @param personID String personID received during login/registration
         * @param authToken String authToken received during login/registration
         */
        public LoadUserTask(Handler handler, String urlString, String personID, String authToken) {
            this.handler = handler;
            this.urlString = urlString;
            this.personID = personID;
            this.authToken = authToken;
        }

        /**
         * Override of run method: creates PersonHandler object, receives the PersonResult object
         * from the server, converts the result into a Person object, and adds the Person object
         * data to the main.DataCache.
         */
        @Override
        public void run() {
            PersonResult personResult = null;
            try {
                personResult = accessServer(urlString, personID, authToken);
                if (personResult != null) {
                    Person person = new Person(
                            personResult.getPerson_id(),
                            personResult.getAssoc_username(),
                            personResult.getFirst_name(),
                            personResult.getLast_name(),
                            personResult.getGender(),
                            personResult.getFather_id(),
                            personResult.getMother_id(),
                            personResult.getSpouse_id()
                    );
                    DataCache dataCache = DataCache.getInstance();
                    dataCache.setUser(person);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error communicating with server");
            }

            sendMessage(personResult);
        }

        /**
         * sendMessage method returns the success boolean and message from the PersonResult object
         * back to the handler in the form of a bundle.
         * @param personResult
         */
        private void sendMessage(PersonResult personResult) {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(ARG_SUCCESS, personResult.isSuccess());
            messageBundle.putString(ARG_MESSAGE, personResult.getMessage());

            message.setData(messageBundle);

            handler.sendMessage(message);
        }
    }

    private PersonResult accessServer(String urlString, String personID, String authToken) throws IOException {
        String urlRegister = urlString + "/person/" + personID;
        URL url = new URL(urlRegister);
        connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");

        connection.addRequestProperty("Authorization", authToken);

        connection.connect();

        PersonResult result = null;
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            Gson gsonDeserializer = new Gson();
            result = gsonDeserializer.fromJson(bufferedReader, PersonResult.class);
        } else {
            result = new PersonResult("Unable to obtain user information");
        }
        return result;
    }
}
