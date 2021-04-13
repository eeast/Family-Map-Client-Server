package HTTPConnection;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.DataCache;
import main.MainActivity;
import model.AuthToken;
import request.LoginRequest;
import result.LoginResult;

public class LoginHandler extends ServerProxy {

    public void login(Fragment fragment, String urlString, LoginRequest loginRequest) {
        System.out.println("Logging in via " + urlString);

        try {
            Handler loginThreadHandler = new Handler() {
                @Override
                public void handleMessage(Message message) {
                    Bundle bundle = message.getData();
                    if (bundle.getBoolean(ARG_SUCCESS)) {
                        Toast.makeText(fragment.getView().getContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                        //Save AuthToken
                        DataCache dataCache = DataCache.getInstance();
                        String authTokenString = bundle.getString(ARG_AUTH_TOKEN);
                        String username = bundle.getString(ARG_USERNAME);
                        AuthToken authToken = new AuthToken(authTokenString, username);
                        dataCache.setAuthToken(authToken);

                        //Start other data handlers
                        UserHandler userHandler = new UserHandler();
                        userHandler.loadUser(fragment.getView(), urlString, bundle.getString(ARG_PERSON_ID));
                        PersonsHandler personsHandler = new PersonsHandler();
                        personsHandler.loadFamily(urlString);
                        EventsHandler eventsHandler = new EventsHandler();
                        eventsHandler.loadFamilyEvents(urlString);

                        //Switch view
                        ((MainActivity)fragment.getActivity()).switchMapView();
                    } else {
                        Toast.makeText(fragment.getView().getContext(), "Unable to authenticate. Please check your username and/or password and try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            LoginTask task = new LoginTask(loginThreadHandler, urlString, loginRequest);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(task);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error encountered during login");
        }
    }

    private class LoginTask implements Runnable {
        private final Handler handler;
        private final String urlString;
        private final LoginRequest loginRequest;

        /**
         * Constructor for LoginTask object
         * @param handler handler created during login method should be passed in
         * @param urlString String url for connection
         * @param loginRequest LoginRequest object containing login details
         */
        public LoginTask(Handler handler, String urlString, LoginRequest loginRequest) {
            this.handler = handler;
            this.urlString = urlString;
            this.loginRequest = loginRequest;
        }

        /**
         * Override of run method: creates LoginHandler object and sends the information from
         * the LoginResult back to the handler in the form of a bundle.
         */
        @Override
        public void run() {
            LoginResult loginResult = null;
            if (loginRequest != null){
                try {
                    loginResult = accessServer(urlString, loginRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error communicating with server");
                }
            }
            assert loginResult != null;
            sendMessage(loginResult);
        }

        /**
         * sendMessage method for conversion of LoginResult to bundle to be sent to the handler
         * @param loginResult LoginResult object received from the server
         */
        private void sendMessage(LoginResult loginResult) {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(ARG_SUCCESS, loginResult.isSuccess());
            messageBundle.putString(ARG_MESSAGE, loginResult.getMessage());
            messageBundle.putString(ARG_AUTH_TOKEN, loginResult.getAuthtoken());
            messageBundle.putString(ARG_USERNAME, loginResult.getUsername());
            messageBundle.putString(ARG_PERSON_ID, loginResult.getPerson_id());
            message.setData(messageBundle);

            handler.sendMessage(message);
        }
    }

    private LoginResult accessServer(String urlString, LoginRequest request) throws IOException {
        String urlLogin = urlString + "/user/login";
        URL url = new URL(urlLogin);
        connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.connect();

        try (OutputStream requestBody = connection.getOutputStream();) {
            sendRequest(request, requestBody);
        }

        LoginResult result = null;
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            Gson gsonDeserializer = new Gson();
            result = gsonDeserializer.fromJson(bufferedReader, LoginResult.class);
        } else {
            result = new LoginResult("Unable to authenticate");
        }
        return result;
    }
}
